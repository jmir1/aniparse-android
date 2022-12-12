package com.github.jmir1.aniparseandroid.library

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test tests the aniparse module with an example filename.
 */
@RunWith(AndroidJUnit4::class)
class ParserTest {

    @Test
    fun parseFilename() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Parser.start(context)
        val testString = "[TaigaSubs]_Toradora!_(2008)_-_01v2_-_Tiger_and_Dragon_[1280x720_H.264_FLAC][1234ABCD].mkv"

        val result = Parser.parse(testString)

        assertNotNull(result)
        val expectedResult = "{'file_name': '[TaigaSubs]_Toradora!_(2008)_-_01v2_-_Tiger_and_Dragon_[1280x720_H.264_FLAC][1234ABCD].mkv', 'file_extension': 'mkv', 'release_group': 'TaigaSubs', 'anime_title': 'Toradora!', 'anime_year': 2008, 'episode_number': 1, 'release_version': 2, 'episode_title': 'Tiger and Dragon', 'video_resolution': '1280x720', 'video_term': 'H.264', 'audio_term': 'FLAC', 'file_checksum': '1234ABCD'}"
        assertEquals(expectedResult, result)
        Parser.stop()
    }
}
