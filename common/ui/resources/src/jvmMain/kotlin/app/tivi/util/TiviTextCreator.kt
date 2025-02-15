// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.tivi.util

import app.tivi.common.ui.resources.MR
import app.tivi.data.models.TiviShow
import app.tivi.inject.ActivityScope
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import java.util.Locale
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toKotlinLocalTime
import me.tatarka.inject.annotations.Inject

@Inject
@ActivityScope
actual class TiviTextCreator(
    override val dateFormatter: TiviDateFormatter,
) : CommonTiviTextCreator {

    override fun airsText(show: TiviShow): CharSequence? {
        val airTime = show.airsTime ?: return null
        val airTz = show.airsTimeZone ?: return null
        val airDay = show.airsDay ?: return null

        val localDateTime = java.time.ZonedDateTime.now(airTz.toJavaZoneId())
            .with(airDay)
            .with(airTime.toJavaLocalTime())
            .withZoneSameInstant(dateFormatter.timeZone.toJavaZoneId())

        return MR.strings.airs_text.format(
            localDateTime.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
            dateFormatter.formatShortTime(localDateTime.toLocalTime().toKotlinLocalTime()),
        ).asString()
    }

    override fun StringDesc.asString(): String = localized()
}
