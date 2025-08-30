package com.secondmind.minimal.v01.data.db

import androidx.room.TypeConverter
import com.secondmind.minimal.v01.data.entities.SeedStatus
import com.secondmind.minimal.v01.data.entities.SeedType
import com.secondmind.minimal.v01.data.entities.SignalSource

class Converters {
    @TypeConverter fun fromSeedType(v: SeedType?): String? = v?.name
    @TypeConverter fun toSeedType(s: String?): SeedType? = s?.let { SeedType.valueOf(it) }
    @TypeConverter fun fromSeedStatus(v: SeedStatus?): String? = v?.name
    @TypeConverter fun toSeedStatus(s: String?): SeedStatus? = s?.let { SeedStatus.valueOf(it) }
    @TypeConverter fun fromSignalSource(v: SignalSource?): String? = v?.name
    @TypeConverter fun toSignalSource(s: String?): SignalSource? = s?.let { SignalSource.valueOf(it) }
}
