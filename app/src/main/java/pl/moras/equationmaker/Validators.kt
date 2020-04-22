package pl.moras.equationmaker

import androidx.core.view.ViewCompat.animate





//check if all edittexts are valid
inline fun isChartValid(function: String, range: String,  functionErrorCallback: ()->Unit, rangeErrorCallback: (Boolean)->Unit): Boolean {
    if (
        isFunctionValid(function) { functionErrorCallback() } &&
        isRangeValid(range, rangeErrorCallback)
    ) {
        return true
    }
    if (!function.contains("x", true)) {
        functionErrorCallback()
    }
    return false
}

inline fun isFunctionValid(function: String, errorCallback: ()->Unit): Boolean {
    if (function.isBlank()) { //check if all edittexts are valid
        errorCallback()
        return false
    }
    return true
}

inline fun areVariablesValid(variables: Array<String>, errorCallback: ()->Unit): Boolean{
    if (variables[0].isBlank()) {
        errorCallback()
        return false
    }
    return true
}

inline fun isRangeValid(range: String, errorCallback: (elo:Boolean)->Unit): Boolean{
    if(range.isBlank()){
        errorCallback(false)
        return false
    } else if (range.toInt() > 100){
        errorCallback(true)
        return false
    }
    return true
}