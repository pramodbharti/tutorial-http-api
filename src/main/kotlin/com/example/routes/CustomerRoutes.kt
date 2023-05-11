package com.example.routes

import com.example.models.Customer
import com.example.models.customerStorage
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty())
                call.respond(customerStorage)
            else
                call.respond("No customers found")
        }

        get("{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respondText("Missing id")
            val customer = customerStorage.find { it.id == id }
                ?: return@get call.respondText("No customer with id: $id")
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText("Customer stored correctly")
        }

        delete("{id?}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText("Missing id")
            val customer = customerStorage.find { it.id == id }
                ?: return@delete call.respondText("No customer with id $id")
            customerStorage.remove(customer)
            call.respondText("Customer deleted correctly")
        }
    }
}