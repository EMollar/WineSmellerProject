package com.projects.winesmeller_v10.others

class Constants {

    companion object {

            // URL SERVER
        val URL_SERVER  = "http://192.168.1.39:8080/developer"

            // Script names
        val SC_USER_AUTHENTICATION  = "userAuthentication.php"
        val SC_USER_LOGIN           = "userLogin.php"
        val SC_ADD_WINE             = "addWine.php"
        val SC_EXIST_BARCODE        = "existBarcode.php"
        val SC_GET_FILLS_BY_BARCODE = "getFillsByBarcode.php"

            // Number Request
        val REQUEST_CAMERA_FRONT_PHOTO      = 1001
        val REQUEST_CAMERA_BACK_PHOTO       = 1002
        val REQUEST_SCAN                    = 0x0000c0de

            // Otros
        val TIME_SPLASH_ACTIVITY = 3000L

    }

}