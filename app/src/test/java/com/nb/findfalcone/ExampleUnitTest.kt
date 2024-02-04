package com.nb.findfalcone

import org.junit.Test
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    lateinit var arr: IntArray

    @Test
    fun addition_isCorrect() {
        //assertEquals(4, 2 + 2)
        arr = IntArray(6)
        arr[0] = 0
        arr[1] = 1
        //println(fibRecursion(5))
        bubbleSort()
        //println(checkTime())
    }

    fun bubbleSort() {
        val arr = arrayOf(3,2,4,1,5)
        for (i in arr.indices) {
            for (j in 0 until  arr.size - i - 1){
                if (arr[j] > arr[j+1]) {
                    val temp = arr[j]
                    arr[j] = arr[j+1]
                    arr[j+1] = temp
                }
            }
        }
        arr.forEach(::println)
    }


    fun fibRecursion(n: Int): Int {
        return when (n) {
            1 -> {
                arr[0]
            }
            2 -> {
                arr[1]
            }
            else -> {
                arr[n-1] = fibRecursion(n - 1) + fibRecursion(n - 2)
                arr[n-1]
            }
        }
    }

    fun checkTime() {
        val listOfTime = arrayOf("12:15PM-02:00PM","09:00AM-10:00AM","10:30AM-12:00PM")
        val df = DateTimeFormatter.ofPattern("hh:mma", Locale.ENGLISH)
        val timeList = listOfTime.map {
            val (f, s) = it.split("-")
            LocalTime.parse(f, df) to LocalTime.parse(s, df)
        }
        val diff = timeList.zipWithNext { a, b ->
            val mins1 = b.first.minute + (b.first.minute*b.first.hour)
            val mins2 = a.second.minute + (a.second.minute*a.second.hour)
            println("$mins1 ${b.first.minute} ${b.first.hour}, $mins2  ${a.second.minute} ${a.second.hour}" )
             mins1 - mins2
        }
        println(diff)

    }










    data class UserData(val name: String, val username: String, val email: String, val id: String) {
        override fun toString(): String {
            return "$name, $username, $email"
        }
    }


    fun LogDumpGetUnique(): String {

        var log_dump : String = "name=John Trust, username=john3, email=john3@gmail.com, id=434453; name=Hannah Smith, username=hsmith, email=hsm@test.com, id=23312; name=Hannah Smith, username=hsmith, id=3223423, email=hsm@test.com; name=Robert M, username=rm44, id=222342, email=rm@me.com; name=Robert M, username=rm4411, id=5535, email=rm@me.com; name=Susan Vee, username=sv55, id=443432, email=susanv123@me.com; name=Robert Nick, username=rnick33, id=23432, email=rnick@gmail.com; name=Robert Nick II, username=rnickTemp34, id=23432, email=rnick@gmail.com; name=Susan Vee, username=sv55, id=443432, email=susanv123@me.com;"

        val arr1 = log_dump.split(";")
        val mutList = mutableSetOf<UserData>()
        arr1.forEach {
            val ls = it.split(",").toTypedArray()
            if (ls.size == 4) {

                val userData = UserData(
                    username = ls.find { it.trim().startsWith("username") } ?: "na",
                    name = ls.find { it.trim().startsWith("name") } ?: "na",
                    email = ls.find { it.trim().startsWith("email") } ?: "na",
                    id = ls.find { it.trim().startsWith("id") } ?: "na"
                )

                mutList.find { it.username == userData.username }?.let {
                    //Element is already present
                } ?: run {
                   // println(userData)
                    if (userData.name != null)
                        mutList.add(userData)
                }
            }
        }
        log_dump = ""
        mutList.distinctBy { it.username }.forEach {
            log_dump += "${it.name}, ${it.username}, ${it.email};"
        }
        return log_dump;

    }
   /* fun LogDumpGetUnique(): String {

        var log_dump : String = "name=John Trust, username=john3, email=john3@gmail.com, id=434453; name=Hannah Smith, username=hsmith, email=hsm@test.com, id=23312; name=Hannah Smith, username=hsmith, id=3223423, email=hsm@test.com; name=Robert M, username=rm44, id=222342, email=rm@me.com; name=Robert M, username=rm4411, id=5535, email=rm@me.com; name=Susan Vee, username=sv55, id=443432, email=susanv123@me.com; name=Robert Nick, username=rnick33, id=23432, email=rnick@gmail.com; name=Robert Nick II, username=rnickTemp34, id=23432, email=rnick@gmail.com; name=Susan Vee, username=sv55, id=443432, email=susanv123@me.com;"

        *//*val arr1 = log_dump.split(";")
        val mutList = mutableSetOf<UserData>()
        arr1.forEach {
            val ls = it.split(",").toTypedArray()
            if (ls.size == 4) {
                //ls.forEach(::println)

                val userData = UserData(
                    username = ls.find { it.trim().startsWith("username") } ?: "na",
                    name = ls.find { it.trim().startsWith("name") } ?: "na",
                    email = ls.find { it.trim().startsWith("email") } ?: "na",
                    id = ls.find { it.trim().startsWith("id") } ?: "na"
                )

                mutList.find { it.username == userData.username }?.let {
                    //Element is already present
                } ?: run {
                    //println(userData.name)
                    if (userData.name != null)
                        mutList.add(userData)
                }
            }
        }
        log_dump = ""
        mutList.distinctBy { it.username }.forEach {
            log_dump += "${it.name}, ${it.username}, ${it.email};\n"
        }*//*

        val users = log_dump.split("; ")
            .map { userInfo ->
                val fields = userInfo.split(", ")
                val name = fields.find { it.startsWith("name=") }?.substringAfter("name=") ?: ""
                val username = fields.find { it.startsWith("username=") }?.substringAfter("username=") ?: ""
                val email = fields.find { it.startsWith("email=") }?.substringAfter("email=") ?: ""
                val id = fields.find { it.startsWith("id=") }?.substringAfter("id=")?.toIntOrNull() ?: 0

                UserData(name, username, email, id.toString())
            }
        val uniqueUsers = users.distinctBy { it.id }

        // Print the unique users
        uniqueUsers.forEach { println(it) }

        return log_dump;

    }*/


/*
    @Test
    fun check_if_selected_vehicle_is_already_sent_on_mission() {

        val ls = mutableListOf(
            //VehicleToPlanet(Planet("Lebrin", 200), Vehicle("Space Pod", 2, 200, 4)),
            VehicleToPlanet(Planet("Donlon", 200), Vehicle("Space Pod", 2, 200, 4))
        )

        VehicleToPlanet(
            Planet("xyz", 200), Vehicle("Space Pod", 2, 200, 4)
        ).let {
            val vPlanetCount = ls.count { vp-> (it.planet?.name == vp.planet?.name) }
            val vehicleCount = ls.count { vp-> (it.vehicle?.name == vp.vehicle?.name) }

            if (ls.size >= 4) {
                println("King! We cannot send more than 4 vehicles.")
            } else if (vPlanetCount != 0 || vehicleCount >= (it.vehicle?.totalNo ?: 0)) {
                println("King! We cannot send this ship to planet")
            } else if ((it.vehicle?.maxDistance ?: 0) < (it.planet?.distance ?: 0)) {
                println("sendVehicleToPlanet: distance not covered")
            } else {
                ls.add(it)
                println("sendVehicleToPlanet: distance covered $ls")
            }
        }
    }*/

}