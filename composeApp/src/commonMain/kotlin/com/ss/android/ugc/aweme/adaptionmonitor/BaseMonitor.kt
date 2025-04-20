package com.ss.android.ugc.aweme.adaptionmonitor


/**
 * @author linshaoqin
 *
 * log util for standard process
 * @see
 */
abstract class BaseMonitor {
    open val eventName: String = "BaseMonitor"
    abstract val enableALog: Boolean
    abstract val enableApmLog: Boolean
    abstract val enableLogcat: Boolean

//    abstract val logs: LogWrapper

    //
//    open fun startByContext(message: MessageGetter? = null) {
//        sendAllLog(ExceptionType.CONTEXT, MonitorAction.START, message)
//    }
    //
//    open fun pauseByContext(message: MessageGetter? = null) {
//        sendAllLog(ExceptionType.CONTEXT, MonitorAction.PAUSE, message)
//    }
    //
//    open fun finishByContext(message: MessageGetter? = null) {
//        sendAllLog(ExceptionType.CONTEXT, MonitorAction.FINISH_1, message)
//    }
    //
//    open fun finishByContext(vararg messagePair: MessagePair?) {
//        sendAllLog(ExceptionType.CONTEXT, MonitorAction.FINISH, messagePair.toList())
//    }

    //
//    open fun startByParam(message: MessageGetter? = null) {
//        sendAllLog(ExceptionType.PARAM, MonitorAction.START, message)
//    }
    //
//    open fun finishByParam(message: MessageGetter? = null) {
//        finishByParam("msg" to message)
//    }
    //
    open fun start(vararg messagePair: MessagePair?) {
        sendAllLog(null, MonitorAction.START, messagePair.toList())
    }
    //
    open fun continueBy(vararg messagePair: MessagePair?) {
        sendAllLog(null, MonitorAction.CONTINUE, messagePair.toList())
    }
    //
    open fun finish(vararg messagePair: MessagePair?) {
        sendAllLog(null, MonitorAction.FINISH, messagePair.toList())
    }
    //
    open fun end(vararg messagePair: MessagePair?) {
        sendAllLog(null, MonitorAction.END, messagePair.toList())
    }
    //
//    open fun startByLogic(message: MessageGetter? = null) {
//        sendAllLog(ExceptionType.LOGIC, MonitorAction.START, message)
//    }
    //
    open fun fail(message: MessageGetter? = null) {
        fail("msg" to message)
    }
    //
    open fun fail(vararg messagePair: MessagePair?) {
        sendAllLog(ExceptionType.LOGIC, MonitorAction.FAILED, messagePair.toList())
    }

    //
    open fun pauseByRequest(message: MessageGetter? = null) {
        sendAllLog(ExceptionType.NET, MonitorAction.PAUSE, message)
    }
    //
    open fun startByResponse(message: MessageGetter? = null) {
        sendAllLog(ExceptionType.NET, MonitorAction.START, message)
    }

    //
    open fun pauseByCalculation(message: MessageGetter? = null) {
        sendAllLog(ExceptionType.CALCULATION, MonitorAction.PAUSE, message)
    }
    //
    open fun startByCalculation(message: MessageGetter? = null) {
        sendAllLog(ExceptionType.CALCULATION, MonitorAction.START, message)
    }

    //
    open fun pauseByUserAction(message: MessageGetter? = null) {
        sendAllLog(ExceptionType.USER_ACTION, MonitorAction.PAUSE, message)
    }
    //
    open fun startByUserAction(message: MessageGetter? = null) {
        sendAllLog(ExceptionType.USER_ACTION, MonitorAction.START, message)
    }


    /**
     *
     */
    private fun sendAllLog(
        type: ExceptionType,
        action: MonitorAction,
        message: MessageGetter? = null
    ) {
        sendAllLog(type, action, listOf("msg" to message))
    }

    private fun sendAllLog(
        type: ExceptionType?,
        action: MonitorAction,
        messagePairs: List<MessagePair?>?
    ) {
        val finalMessage = {
            var tmpString = action.describe
            if (type != null) {
                tmpString = tmpString.plus(",by ${type.describe}")
            }
            messagePairs?.filterNotNull()?.forEach {
                tmpString = tmpString.plus(",${it.first}:${it.second?.invoke()}")
            }
            tmpString
        }
        if (enableLogcat) {
//            if (action.logLevel == Log.ERROR) {
//                Log.e(eventName, finalMessage())
//            } else if (action.logLevel == Log.WARN) {
//                Log.w(eventName, finalMessage())
//            } else if (action.logLevel == Log.INFO) {
//                Log.i(eventName, finalMessage())
//            } else if (action.logLevel == Log.DEBUG) {
//                Log.d(eventName, finalMessage())
//            } else if (action.logLevel == Log.VERBOSE) {
//                Log.v(eventName, finalMessage())
//            }
        }
//        if (enableALog) {
//            if (action.logLevel == Log.ERROR) {
//                logs.error(finalMessage)
//            } else if (action.logLevel == Log.WARN) {
//                logs.warn(finalMessage)
//            } else if (action.logLevel == Log.INFO) {
//                logs.info(finalMessage)
//            } else if (action.logLevel == Log.DEBUG) {
//                logs.debug(finalMessage)
//            } else if (action.logLevel == Log.VERBOSE) {
//                Log.v(eventName, finalMessage())
//            }
//        }
//        if (enableApmLog && action.logLevel >= apmLogMinLevel) {
//            sendApmLog(
//                EXCEPTION_TYPE_KEY to type.describe,
//                SCENE_KEY to action.name,
//                LEVEL_KEY to action.logLevel.toString(),
//                MESSAGE_KEY to message?.invoke().toString()
//            )
//        }

    }

//    private fun sendApmLog(vararg pairs: Pair<String, String>) {
//        try {
//            CoroutineScope(Dispatchers.IO).launch {
//                val category = JSONObject().apply {
//                    pairs.forEach {
//                        put(it.first, it.second)
//                    }
//                }
//                ApmAgent.monitorEvent(eventName, category, null, null)
//            }
//        } catch (e: Exception) {
//
//        }
//    }
}

//
typealias MessageGetter = () -> Any?

//
typealias MessagePair = Pair<String, MessageGetter?>

// if block by invalid logic step, report -> error
// if block by condition, report exception -> info
// if invalid callback null (blocking by framework/user), report this and former step success -> info
enum class ExceptionType(
    val describe: String,
) {
    CONTEXT("ctx"), // trigger continue / finish
    PARAM("param"), // trigger continue / finish
    LOGIC("logic"), // trigger continue / failed

    NET("net"), // trigger pause / start
    DB("db"), // trigger pause / start
    CALCULATION("calc"), // trigger pause / start
    USER_ACTION("usr"), // trigger pause / start
}

// action will trigger
enum class MonitorAction(
    val describe: String,
    val logLevel: Int?,
) {
    INVALID("invalid", null),
    PAUSE("pause", null), // module process pause, wait from other module / use to start
    START("start", null), // module process start
    FAILED("fail", null), // finish with failed logic and end module process
    CONTINUE("continue", null), // continue to next
    FINISH("finish", null), // finish within module condition branch, not end module process
    END("end", null), // finish with success logic and end module process
}

// sequence of params will determine log level
enum class MonitorStatus(val describe: String) {
    IDLE("idle"),
    STARTED("started"),
    PAUSED("paused"),
    END("end"),
}


private const val SCENE_KEY = "scene"
private const val EXCEPTION_TYPE_KEY: String = "exception_type"
private const val LEVEL_KEY = "level"
private const val MESSAGE_KEY = "message"