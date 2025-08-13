package com.oshanak.mobilemarket.Activity.Service;


public class TaskResult
{
    public boolean isSuccessful = false;
    public String message = "";
    public Object dataStructure = null;
    public String tag = "";

    public TaskResult() {
    }

    public TaskResult(boolean isSuccessful, String message, Object dataStructure, String tag) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        this.dataStructure = dataStructure;
        this.tag = tag;
    }

    public boolean isExceptionOccured(String exceptionKeyWord)
    {
        return message.toLowerCase().contains(exceptionKeyWord.toLowerCase());
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "isSuccessful=" + isSuccessful +
                ", message='" + message + '\'' +
                ", dataStructure=" + dataStructure +
                ", tag='" + tag + '\'' +
                '}';
    }
}
