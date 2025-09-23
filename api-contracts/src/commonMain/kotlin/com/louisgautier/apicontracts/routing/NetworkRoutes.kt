package com.louisgautier.apicontracts.routing

import io.ktor.resources.Resource

@Resource("/")
class Root {

    @Resource("admin")
    class Admin(val parent: Root = Root()) {
        @Resource("swagger")
        class Swagger(val parent: Admin = Admin())

        @Resource("openapi")
        class OpenAPI(val parent: Admin = Admin())

        @Resource("metrics")
        class Metrics(val parent: Admin = Admin())
    }

    @Resource("register")
    class Register(val parent: Root = Root())

    @Resource("register_anon")
    class RegisterAnonymously(val parent: Root = Root())

    @Resource("login")
    class Login(val parent: Root = Root())

    @Resource("/me")
    class Me(val parent: Root = Root())

    @Resource("/refresh_token")
    class RefreshToken(val parent: Root = Root())

    @Resource("notes")
    class Notes(val parent: Root = Root(), val page: Int? = null, val limit: Int? = null) {
        @Resource("{id}")
        class Id(val parent: Notes = Notes(), val id: Int)
    }
}