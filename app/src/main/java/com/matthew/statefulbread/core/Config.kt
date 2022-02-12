package com.matthew.statefulbread.core

 interface Config {
     companion object {
         val APP_NAME = "StatefulBread"
         val DATABASE_NAME = APP_NAME
         val SHAREDPREFERENCES_NAME = APP_NAME
     }
 }