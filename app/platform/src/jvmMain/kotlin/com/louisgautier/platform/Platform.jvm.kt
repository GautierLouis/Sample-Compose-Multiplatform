package com.louisgautier.platform


actual fun platform() = "Java ${System.getProperty("java.version")}"