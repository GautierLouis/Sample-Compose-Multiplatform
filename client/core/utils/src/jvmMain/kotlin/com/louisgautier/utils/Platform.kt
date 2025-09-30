package com.louisgautier.utils

actual fun platform() = "Java ${System.getProperty("java.version")}"