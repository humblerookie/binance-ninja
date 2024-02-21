package dev.anvith.binanceninja.data.remote.serialization

import dev.anvith.binanceninja.data.remote.models.AreaType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AreaTypeSerializer : KSerializer<AreaType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AreaType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AreaType) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): AreaType {
        val string = decoder.decodeString()
        return AreaType.entries.firstOrNull { it.value == string } ?: AreaType.UNKNOWN
    }
}