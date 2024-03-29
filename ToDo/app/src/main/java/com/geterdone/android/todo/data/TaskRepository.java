package com.geterdone.android.todo.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
        mAllTasks = mTaskDao.getAllTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    void insert(Task task) {
        new insertAsyncTask(mTaskDao).execute(task);
    }

    void update(Task task) {
        new updateAsyncTask(mTaskDao).execute(task);
    }

    void delete(Task task) {
        new deleteAsyncTask(mTaskDao).execute(task);
    }

    Task getTaskById(int mId) {
        AsyncTask<Integer, Void, Task> asyncTask = new getTaskByIdAsyncTask(mTaskDao).execute(mId);
        Task task = null;
        try {
            task = asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return task;
    }

    //helper classes
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao mAsyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        updateAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;

        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.update(tasks[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mAsyncTaskDao;

        private deleteAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.delete(tasks[0]);
            return null;
        }
    }


    private static class getTaskByIdAsyncTask extends AsyncTask<Integer, Void, Task> {
        private TaskDao mAsyncTaskDao;

        private getTaskByIdAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Task doInBackground(Integer... integers) {
            return mAsyncTaskDao.getTaskById(integers[0]);
        }
    }
}
