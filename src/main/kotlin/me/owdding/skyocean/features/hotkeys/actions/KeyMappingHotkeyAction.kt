package me.owdding.skyocean.features.hotkeys.actions

import com.mojang.serialization.MapCodec
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.lib.builder.LayoutFactory
import me.owdding.skyocean.SkyOcean.id
import me.owdding.skyocean.features.hotkeys.WidgetContext
import me.owdding.skyocean.generated.SkyOceanCodecs
import me.owdding.skyocean.utils.chat.CatppuccinColors
import me.owdding.skyocean.utils.extensions.createText
import me.owdding.skyocean.utils.extensions.createTextInput
import me.owdding.skyocean.utils.extensions.topLeft
import me.owdding.skyocean.utils.extensions.withPadding
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.layouts.LayoutElement
import earth.terrarium.olympus.client.utils.ListenableState

@GenerateCodec
data class KeyMappingHotkeyAction(
    @FieldName("key_name") var keyName: String,
) : HotkeyAction {
    override val codec: MapCodec<KeyMappingHotkeyAction> = SkyOceanCodecs.KeyMappingHotkeyActionCodec
    override val type: HotkeyActionType = HotkeyActionType.KEY_MAPPING

    context(context: WidgetContext)
    override fun asLayoutElement(selector: LayoutElement): LayoutElement = LayoutFactory.vertical {
        widget(selector, topLeft)
        createText("Key mapping name", CatppuccinColors.Mocha.surface0).withPadding(left = 5).add()
        val state = ListenableState.of(keyName)
        state.registerListener { keyName = it }
        createTextInput(
            state = state,
            placeholder = "Key mapping name",
            texture = id(context.listBackground),
            width = context.width - 10,
        ).withPadding(bottom = 5).add()
    }

    override fun perform() {
        KeyMapping.get(keyName)?.clickCount++
    }

    override fun duplicate(): HotkeyAction = copy()
}
