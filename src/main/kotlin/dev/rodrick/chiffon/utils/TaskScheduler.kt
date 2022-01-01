package dev.rodrick.chiffon.utils

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import java.util.*

object TaskScheduler {
    private var delayedTasks = LinkedList<Pair<Int, () -> Unit>>()
    private var tick = 0

    init {
        ServerTickEvents.END_WORLD_TICK.register {
            tick++

            synchronized(TaskScheduler) {
                while (delayedTasks.isNotEmpty()) {
                    val (time, target) = delayedTasks.last
                    if (time > tick) {
                        break
                    }
                    target()
                    delayedTasks.removeLast()
                }
            }
        }
    }

    fun scheduleDelayed(ticks: Int, task: () -> Unit): () -> Unit {
        val el = ticks + tick to task
        synchronized(TaskScheduler) {
            delayedTasks.add(el)
            delayedTasks.sortByDescending { it.first }
        }

        return {
            synchronized(TaskScheduler) {
                delayedTasks.removeIf { it === el }
            }
        }
    }
}
