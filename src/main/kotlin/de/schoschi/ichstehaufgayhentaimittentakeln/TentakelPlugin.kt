package de.schoschi.ichstehaufgayhentaimittentakeln

import com.destroystokyo.paper.block.TargetBlockInfo
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class TentakelPlugin : JavaPlugin(), CommandExecutor, Listener {

    var isChallengeEnabled = false

    override fun onEnable() {
        // Plugin startup logic
        getLogger().info("TentakelPlugin enabled")
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            if (isChallengeEnabled) {
                giveplayersblock()
            }
        }, 0, 5)
        Bukkit.getPluginCommand("challenge")!!.setExecutor(this)
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            actionbar()
        }, 0, 5)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        event.isDropItems = !isChallengeEnabled
        if (isChallengeEnabled) {
            event.player.sendMessage("Blockbreaking is disabled")
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        getLogger().info("TentakelPlugin disabled")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        isChallengeEnabled = !isChallengeEnabled
        sender.sendMessage("TentakelPlugin is now ${if (isChallengeEnabled) "enabled" else "disabled"}")
        return true
    }

    fun giveplayersblock() {
        for (player in Bukkit.getOnlinePlayers()) {
            val targetblock = player.getTargetBlock(5, TargetBlockInfo.FluidMode.NEVER) ?: continue
            if (targetblock.type == Material.AIR) continue
            player.inventory.addItem(ItemStack(targetblock.type, 1))
        }
    }

    fun actionbar() {
        Bukkit.getOnlinePlayers().forEach {
            it.sendActionBar(
                Component.text(
                    "Challange " +
                            if (isChallengeEnabled) {
                                "Enabled"
                            } else {
                                "Disabled"
                            }
                )
            )
        }

    }
}
