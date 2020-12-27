package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import technokek.alchotracker.R
import technokek.alchotracker.data.models.CalendarModel
import kotlin.random.Random

class CalendarLiveData() : MutableLiveData<MutableMap<LocalDate, MutableList<CalendarModel>>>() {
    private lateinit var query: Query
    private val eventsListener = EventsListener()
    private lateinit var uRef: Query
    private val mAuth = FirebaseAuth.getInstance()

    constructor(eRef: DatabaseReference, uRef: DatabaseReference) : this() {
        query = eRef
        this.uRef = uRef
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(eventsListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(eventsListener)
        Log.d(FriendLiveData.TAG, "onInactive")
    }

    companion object {
        const val TAG = "CalendarLiveData"
        const val AVATAR = "avatar"
        const val DATE = "date"
        const val EVENT_ID = "id"
        const val MEMBERS = "members"
        const val ADMIN_ID = "adminid"
        const val NAME = "name"
        const val PLACE = "place"
        const val PRICE = "price"
        const val TIME = "time"
        const val ORDINARY_MEMBERS = "users"
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        private const val stringSize: Int = 16

        const val EVENTS = "events"
        const val ADMIN = "admin"
        const val TRUE = "true"
        const val FALSE = "false"
        const val USERS_CLICKED = "users_clicked"

        fun stringDateToLocalDate(date: String): LocalDate {
            val dateInStringList: List<String?> = date.split(
                delimiters = charArrayOf('-'),
                limit = 3
            )
            Log.d("CheckNull", date)
            try {
                return LocalDate.of(
                    dateInStringList[0]!!.toInt(),
                    dateInStringList[1]!!.toInt(),
                    dateInStringList[2]!!.toInt()
                )
            } catch (e: Exception) {
                return LocalDate.of(
                    2020,
                    11,
                    1
                )
            }
        }

        fun timeToLocalDateTime(date: LocalDate, time: String): LocalDateTime {
            val timeInStringList = time.split(
                delimiters = charArrayOf(':'),
                limit = 2
            )
            try {
                return date.atTime(
                    timeInStringList[0].toInt(),
                    timeInStringList[1].toInt()
                )
            } catch (e: Exception) {
                return date.atTime(
                    0,
                    0
                )
            }
        }

        fun generateRandomStringEventId(): String {
            return (1..stringSize)
                .map { i -> Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
        }

        fun localDateTimeToTimeString(localDateTime: LocalDateTime): String {
            return localDateTime.toString().substringAfter('T')
        }
    }

    fun pushDataToDB(date: LocalDate, event: CalendarModel) {
        if (!value!!.containsKey(date)) {
            // Кладём значение в liveData
            value!![date] = mutableListOf(event)
            // Записываем в БД по ссылке
            pushEventToBD(date, event, generateRandomStringEventId())
        } else {
            // Кладём значение в liveData
            val eventsThisDate = value!![date]
            eventsThisDate!!.add(event)
            value!![date] = eventsThisDate
            // Записываем в БД по ссылке
            pushEventToBD(date, event, generateRandomStringEventId())
        }
    }

    private fun pushEventToBD(date: LocalDate, event: CalendarModel, eventNumber: String) {
        pushEventToUsersDB(event, eventNumber)
        pushEventToEventsDB(date, event, eventNumber)
    }

    private fun pushEventToEventsDB(date: LocalDate, event: CalendarModel, eventNumber: String) {
        query.ref.child(eventNumber).apply {
            child(AVATAR).setValue(event.avatar)
            child(DATE).setValue(date.toString())
            child(EVENT_ID).setValue(eventNumber)
            child(MEMBERS)
                .child(ADMIN_ID)
                .setValue(event.adminId)
            child(MEMBERS)
                .child(ORDINARY_MEMBERS)
                .setValue("")
            child(NAME).setValue(event.eventPlace.name)
            child(PLACE).setValue(event.eventPlace.place)
            child(PRICE).setValue(event.eventPlace.price)
            child(TIME).setValue(localDateTimeToTimeString(event.time))
        }
    }

    private fun pushEventToUsersDB(event: CalendarModel, eventNumber: String) {
        uRef.ref.child(event.adminId).apply {
            child(EVENTS)
                .child(eventNumber)
                .child(ADMIN).setValue(TRUE)
        }
    }

    fun pushOnMemberDenied(calendarModel: CalendarModel) {
        val usersClicked:String
        if (calendarModel.usersClickedIDs.equals("")) {
            usersClicked = mAuth.currentUser?.uid.toString()
        }
        else {
            usersClicked = "${calendarModel.usersClickedIDs};${mAuth.currentUser?.uid.toString()}"
        }
        query.ref.child(calendarModel.id).apply {
            child(MEMBERS)
                .child(USERS_CLICKED)
                .setValue(usersClicked)
        }
    }

    fun pushOnMemberAccepted(calendarModel: CalendarModel) {
        pushOnMemberAcceptedInEventsDB(calendarModel)
        pushOnMemberAcceptedInUsersDB(calendarModel)
    }

    private fun pushOnMemberAcceptedInEventsDB(calendarModel: CalendarModel) {
        val membersIds:String
        if (calendarModel.ordinaryMembersIds.equals("")) {
            membersIds = mAuth.currentUser?.uid.toString()
        }
        else {
            membersIds = "${calendarModel.ordinaryMembersIds};${mAuth.currentUser?.uid.toString()}"
        }
        val usersClicked:String
        if (calendarModel.usersClickedIDs.equals("")) {
            usersClicked = mAuth.currentUser?.uid.toString()
        }
        else {
            usersClicked = "${calendarModel.usersClickedIDs};${mAuth.currentUser?.uid.toString()}"
        }
        query.ref.child(calendarModel.id).apply {
            child(MEMBERS)
                .child(ORDINARY_MEMBERS)
                .setValue(membersIds)
            child(MEMBERS)
                .child(USERS_CLICKED)
                .setValue(usersClicked)
        }
    }

    private fun pushOnMemberAcceptedInUsersDB(calendarModel: CalendarModel) {
        uRef.ref.child(mAuth.currentUser?.uid.toString()).apply {
            child(EVENTS)
                .child(calendarModel.id)
                .child(ADMIN).setValue(FALSE)
        }
    }

    fun pushDeleteEventToDB(date: LocalDate, calendarModel: CalendarModel) {
        // TODO delete only possible if its admin of the event
        if (value!![date]!!.size == 1) {
            value!!.remove(date)
        } else {
            val todayEvents = value!![date]
            todayEvents!!.remove(calendarModel)
            value!![date] = todayEvents
        }
        if (mAuth.currentUser?.uid.toString().equals(calendarModel.adminId)) {
            pushDeleteAsAdmin(calendarModel)
        }
        else {
            pushDeleteAsOrdinaryMember(calendarModel)
        }
    }

    private fun pushDeleteAsAdmin(calendarModel: CalendarModel) {
        val allMembers: MutableList<String> = calendarModel.ordinaryMembersIds.split(";").toMutableList()
        allMembers.add(calendarModel.adminId)
        //delete event
        query.ref.child(calendarModel.id).removeValue()

        //delete in users
        allMembers.forEach {memberId ->
            uRef.ref.child(memberId)
                .child(EVENTS)
                .child(calendarModel.id).removeValue()
        }
    }

    private fun pushDeleteAsOrdinaryMember(calendarModel: CalendarModel) {
        //In events
        val allMembers: MutableList<String> = calendarModel.ordinaryMembersIds.split(";").toMutableList()
        if (allMembers.contains(mAuth.currentUser?.uid.toString())) {
            allMembers.remove(mAuth.currentUser?.uid.toString())
        }
        var ordinaryMembers = ""
        allMembers.forEachIndexed { index, s ->
            if (index == allMembers.size - 1) {
                ordinaryMembers += allMembers[index]
            }
            else {
                ordinaryMembers += "${allMembers[index]};"
            }
        }
        val allMembersClicked: MutableList<String> = calendarModel.usersClickedIDs.split(";").toMutableList()
        if (allMembersClicked.contains(mAuth.currentUser?.uid.toString())) {
            allMembersClicked.remove(mAuth.currentUser?.uid.toString())
        }
        var usersClicked = ""
        allMembersClicked.forEachIndexed { index, s ->
            if (index == allMembersClicked.size - 1) {
                usersClicked += allMembersClicked[index]
            }
            else {
                usersClicked += "${allMembersClicked[index]};"
            }
        }
        query.ref.child(calendarModel.id).apply {
            child(MEMBERS)
                .child(ORDINARY_MEMBERS)
                .setValue(ordinaryMembers)
            child(MEMBERS)
                .child(USERS_CLICKED)
                .setValue(usersClicked)
        }


        //In users
        uRef.ref.child(mAuth.currentUser?.uid.toString())
            .child(EVENTS)
            .child(calendarModel.id).removeValue()
    }

    inner class EventsListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val calendarEvents: MutableMap<LocalDate, MutableList<CalendarModel>> = mutableMapOf()

            for (i in snapshot.children) {
                // Get data
                retrieveData(i, calendarEvents)
            }
            /*if (calendarEvents.size > value!!.size) {
                val difference =
                    calendarEvents.filter {
                    !value!!.keys.contains(it.key)
                }
            }*/
            value = calendarEvents
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }



        private fun retrieveData(
            i: DataSnapshot,
            calendarEvents: MutableMap<LocalDate, MutableList<CalendarModel>>
        ) {
            val localDate = stringDateToLocalDate(i.child("date").value.toString())
            val localDateTime = timeToLocalDateTime(
                localDate,
                i.child("time").value.toString()
            )
            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val calendarEvent = CalendarModel(
                time = localDateTime,
                CalendarModel.Place(
                    name = i.child("name").value.toString(),
                    price = i.child("price").value.toString(),
                    place = i.child("place").value.toString(),
                ),
                color = R.color.primaryColor,
                id = i.child("id").value.toString(),
                adminId = i.child("members")
                    .child(ADMIN_ID).value.toString(),
                ordinaryMembersIds = i.child(MEMBERS)
                    .child(ORDINARY_MEMBERS).value.toString(),
                userClicked = i.child(MEMBERS)
                    .child(USERS_CLICKED).value.toString()
                    .contains(mAuth.currentUser?.uid.toString())
            )
            if (!calendarEvents.containsKey(localDate)) {
                calendarEvents.put(localDate, mutableListOf(calendarEvent))
            } else {
                val eventsThisDate = calendarEvents[localDate]
                eventsThisDate!!.add(calendarEvent)
                calendarEvents.put(localDate, eventsThisDate)
            }
        }
    }
}
