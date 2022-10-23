package com.norihiro.myapplication

import com.unity3d.player.UnityPlayer

class CommunicationBridge(val communicationCallback: CommunicationCallback) {
    fun callFromUnityWithNoParameters() {
        communicationCallback.onNoParamCall()
    }
    fun callFromUnityWithOneParameter(param: String) {
        communicationCallback.onOneParamCall(param)
    }

    fun callFromUnityWithTwoParameters(param1: String, param2: Int){
        communicationCallback.onTwoParamCall(param1, param2)
    }

    fun callToUnityWithNoMessage() {
        UnityPlayer.UnitySendMessage("ApiHandler", "CallFromAndroidWithNoMessage", "")
    }

    fun callToUnityShowMainScene() {
        UnityPlayer.UnitySendMessage("ApiHandler", "ShowMainScene", "")
    }

    fun callToUnityShowScene2() {
        UnityPlayer.UnitySendMessage("ApiHandler", "ShowScene2", "")
    }

    fun callToUnityWithMessage(param: String) {
        UnityPlayer.UnitySendMessage("ApiHandler", "CallFromAndroidWithMessage", param)
    }

    fun callToUnitySendJson(param: String) {
        UnityPlayer.UnitySendMessage("ApiHandler", "pafeInfo", param)
    }
    interface CommunicationCallback {
        fun onNoParamCall()
        fun onOneParamCall(param: String)
        fun onTwoParamCall(param1: String, param2: Int)
    }
}