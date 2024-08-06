package com.swissborg.challenge.data.serializer

import androidx.compose.ui.input.key.Key.Companion.T
import com.swissborg.challenge.data.scheme.TradingPairScheme
import com.swissborg.challenge.util.TestTradingPairJson
import com.swissborg.challenge.util.TestTradingPairScheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class TradingPairSchemeSerializerTest {

    @Test
    fun `deserialize json to scheme model`() {
        val expected = TestTradingPairScheme
        val actual = Json.decodeFromString(
            deserializer = TradingPairSchemeSerializer,
            string = TestTradingPairJson,
        )
        assertEquals(
            expected = expected,
            actual = actual,
            message = "Trading pair is not properly deserialized"
        )
    }

}
