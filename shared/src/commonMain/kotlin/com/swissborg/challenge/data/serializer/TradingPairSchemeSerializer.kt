package com.swissborg.challenge.data.serializer

import com.swissborg.challenge.data.scheme.TradingPairScheme
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonPrimitive

internal object TradingPairSchemeSerializer : KSerializer<TradingPairScheme> {

    private val serializer = JsonArray.serializer()

    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun deserialize(decoder: Decoder): TradingPairScheme =
        decoder.decodeSerializableValue(deserializer = serializer).let { array ->
            TradingPairScheme(
                symbol = array[0].jsonPrimitive.content,
                bid = array[1].jsonPrimitive.content,
                bidSize = array[2].jsonPrimitive.content,
                ask = array[3].jsonPrimitive.content,
                askSize = array[4].jsonPrimitive.content,
                dailyChange = array[5].jsonPrimitive.content,
                dailyChangeRelative = array[6].jsonPrimitive.content,
                lastPrice = array[7].jsonPrimitive.content,
                volume = array[8].jsonPrimitive.content,
                high = array[9].jsonPrimitive.content,
                low = array[10].jsonPrimitive.content,
            )
        }

    override fun serialize(encoder: Encoder, value: TradingPairScheme) {
        // Nothing as we don't need to serialize it yet
    }

}
