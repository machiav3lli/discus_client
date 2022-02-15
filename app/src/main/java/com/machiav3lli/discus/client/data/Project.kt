package com.machiav3lli.discus.client.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.util.*

@Entity
@Serializable
data class Project(
    var question: String = "new project",
    var replyPositive: String = "",
    var replyNeutral: String = "",
    var replyNegative: String = "",
    @Required
    var argumentsPositive: MutableList<String> = mutableListOf("", "", ""),
    @Required
    var argumentsNegative: MutableList<String> = mutableListOf("", "", ""),
    var webpage: String = "",
    var invitation: String = "",
    @Serializable(with = CalendarSerializer::class)
    var meetingDateTime: Calendar = Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
    }
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var hash = 7
        hash = 31 * hash + id.toInt()
        hash = 31 * hash + question.hashCode()
        hash = 31 * hash + replyPositive.hashCode()
        hash = 31 * hash + replyNeutral.hashCode()
        hash = 31 * hash + replyNegative.hashCode()
        hash = 31 * hash + argumentsPositive.hashCode()
        hash = 31 * hash + argumentsNegative.hashCode()
        hash = 31 * hash + webpage.hashCode()
        hash = 31 * hash + invitation.hashCode()
        hash = 31 * hash + meetingDateTime.hashCode()
        return hash
    }

    override fun toString(): String = "Project{" +
            "id=" + id +
            ", question=" + question +
            ", replyPositive=" + replyPositive +
            ", replyNeutral=" + replyNeutral +
            ", replyNegative=" + replyNegative +
            ", argumentsPostive=" + argumentsPositive +
            ", argumentsNegative=" + argumentsNegative +
            ", pageLink=" + webpage +
            ", meetingInvitation=" + invitation +
            ", meetingTime=" + meetingDateTime +
            '}'

    class Builder {
        val project: Project = Project()

        fun withId(id: Long): Builder {
            project.id = id
            return this
        }

        fun import(export: Project): Builder {
            project.question = export.question
            project.webpage = export.webpage
            project.replyPositive = export.replyPositive
            project.replyNeutral = export.replyNeutral
            project.replyNegative = export.replyNegative
            project.argumentsPositive = export.argumentsPositive
            project.argumentsNegative = export.argumentsNegative
            project.invitation = export.invitation
            project.meetingDateTime = export.meetingDateTime
            return this
        }

        fun build(): Project {
            return project
        }
    }

    fun toJSON() = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String) = Json.decodeFromString<Project>(json)

        @Serializer(forClass = Calendar::class)
        object CalendarSerializer : KSerializer<Calendar> {
            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("CalendarSerializer", PrimitiveKind.LONG)

            override fun serialize(output: Encoder, obj: Calendar) {
                output.encodeLong(obj.time.time)
            }

            override fun deserialize(input: Decoder): Calendar {
                return Calendar.getInstance().apply { timeInMillis = input.decodeLong() }
            }
        }
    }
}
