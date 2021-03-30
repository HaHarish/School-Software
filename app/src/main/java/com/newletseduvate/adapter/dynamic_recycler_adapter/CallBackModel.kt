package com.newletseduvate.adapter.dynamic_recycler_adapter

/**
 * Created by SHASHANK BHAT on 28-Feb-21.
 *
 *
 */

class CallBackModel<MODEL_TYPE>(val id : Int, val block: (model:MODEL_TYPE, position:Int) -> Unit)