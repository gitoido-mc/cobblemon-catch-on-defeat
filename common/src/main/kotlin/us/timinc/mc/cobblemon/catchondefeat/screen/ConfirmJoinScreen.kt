package us.timinc.mc.cobblemon.catchondefeat.screen

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2
import org.lwjgl.glfw.GLFW
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.modResource
import us.timinc.mc.cobblemon.catchondefeat.network.JoinConfirmReceipt


class ConfirmJoinScreen(private val packet: JoinConfirmReceipt.Packet) :
    Screen(Component.translatable(modResource("confirm_join").toLanguageKey())) {
    companion object {
        // Screen root widget params
        const val BASE_WIDTH = 300
        const val BASE_HEIGHT = 80

        // Proceed button widget params
        const val GUI_PROCEED_WIDTH = 50f
        const val GUI_PROCEED_HEIGHT = 20f
        const val GUI_PROCEED_OFFSET_X = 10f
        const val GUI_PROCEED_OFFSET_Y = 30f

        // Cancel button widget params
        const val GUI_CANCEL_WIDTH = 50f
        const val GUI_CANCEL_HEIGHT = 20f
        const val GUI_CANCEL_OFFSET_X = 60f
        const val GUI_CANCEL_OFFSET_Y = 30f
    }

    private var responded: Boolean = false

    val middleX: Int
        get() = this.minecraft!!.window.guiScaledWidth / 2
    val middleY: Int
        get() = this.minecraft!!.window.guiScaledHeight / 2

    val leftX: Int
        get() = middleX - BASE_WIDTH / 2
    val topY: Int
        get() = middleY - BASE_HEIGHT / 2

    override fun init() {


        createButton(
            CommonComponents.GUI_PROCEED,
            Vec2(
                GUI_PROCEED_WIDTH,
                GUI_PROCEED_HEIGHT
            ),
            Vec2(
                leftX + GUI_PROCEED_OFFSET_X,
                topY + (BASE_HEIGHT - GUI_PROCEED_OFFSET_Y),
            ),
        ) {
            responded = true
            packet.accept()
            onClose()
        }.also {
            this.addRenderableWidget(it)
        }

        createButton(
            CommonComponents.GUI_CANCEL,
            Vec2(
                GUI_CANCEL_WIDTH,
                GUI_CANCEL_HEIGHT
            ),
            Vec2(
                leftX + (BASE_WIDTH - GUI_CANCEL_OFFSET_X),
                topY + (BASE_HEIGHT - GUI_CANCEL_OFFSET_Y),
            ),
        ) {
            responded = false
            packet.reject()
            onClose()
        }.also {
            this.addRenderableWidget(it)
        }

        super.init()
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            responded = false
            packet.reject()
            this.onClose()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    /**
     * Button creation helper function
     *
     * @param label Accepts `Component` instance with screen heading
     * @param size `Vec2i` holder for button size dimensions
     * @param pos `Vec2i` holder for button position
     * @param callback Callback function which fires on button click
     * @return Button
     */
    fun createButton(label: Component, size: Vec2, pos: Vec2, callback: (Button) -> Unit): Button = Button
        .builder(label, callback)
        .size(size.x.toInt(), size.y.toInt())
        .pos(pos.x.toInt(), pos.y.toInt())
        .build()
}
