package me.owdding.skyocean.features.hotkeys.conditions

import com.mojang.serialization.MapCodec
import earth.terrarium.olympus.client.utils.ListenableState
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
import net.minecraft.client.gui.layouts.LayoutElement
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.extentions.getSkyBlockId

@GenerateCodec
data class HeldItemHotkeyCondition(
    var itemId: String = "",
) : HotkeyCondition {
    override val codec: MapCodec<out HotkeyCondition> = SkyOceanCodecs.HeldItemHotkeyConditionCodec
    override val type: HotkeyConditionType = HotkeyConditionType.HELD_ITEM

    override fun test(): Boolean =
        McClient.self.player?.mainHandItem?.getSkyBlockId().equals(itemId.trim(), ignoreCase = true)

    override fun describe() = "Holding: ${itemId.ifBlank { "any item" }}"

    context(context: WidgetContext)
    override fun asLayoutElement(selector: LayoutElement): LayoutElement = LayoutFactory.vertical {
        widget(selector, topLeft)
        createText("SkyBlock item ID", CatppuccinColors.Mocha.surface0).withPadding(left = 5).add()
        val state = ListenableState.of(itemId)
        state.registerListener { itemId = it }
        createTextInput(
            state = state,
            placeholder = "e.g. HYPERION",
            texture = id(context.listBackground),
            width = context.width - 10,
        ).withPadding(bottom = 5).add()
    }

    override fun duplicate(): HotkeyCondition = copy()
}
