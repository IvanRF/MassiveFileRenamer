/*
 * Copyright (C) 2014 Ivan Ridao Freitas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivanrf.renamer.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.SwingWorker;

import com.ivanrf.renamer.visual.Principal;

/**
 * @param <T> the result type returned by this {@code SwingWorker's}
 *        {@code doInBackground} and {@code get} methods
 * @param <V> = Object, the type used for carrying out intermediate results by this
 *        {@code SwingWorker's} {@code publish} and {@code process} methods
 */
public abstract class SwingWorkerExt<T> extends SwingWorker<T, Object> implements PropertyChangeListener {
	
	private long maximum;
	private long value;
	private boolean percentProgressBarStarted;
	private boolean sleepForInterrupt; //Allows cancelling the process
	
	public SwingWorkerExt() {
		super();
		maximum = 1;
		value = 0;
		percentProgressBarStarted = false;
		sleepForInterrupt = false;
		addPropertyChangeListener(this);
	}
	
	public void publishEstado(Object estado) {
		publish(estado);
	}
	
	@Override
	protected void process(List<Object> estados) {
		Principal.getInstance().setEstado(estados.get(estados.size()-1).toString());
	}
	
	private void setProgressValue(long value) throws InterruptedException {
		int progress = (int)((value*100) / maximum);
		if(progress>100)
			progress=100;
		setProgress(progress);
		if (sleepForInterrupt && 0==progress%2) { //Sleeps for 1 millisecond each 2% of work done
			Thread.sleep(1L);
		}
	}

	public void setMaximum(long maximum) throws InterruptedException {
		if(maximum>0){
			this.maximum = maximum;
			value = 0;
			percentProgressBarStarted = false;
			setProgressValue(value);
		}
	}

	public void addProgressValue() throws InterruptedException {
		value++;
		setProgressValue(value);
	}
	
	public void addProgressValue(long add) throws InterruptedException {
		value+=add;
		setProgressValue(value);
	}
	
	/**
	 * Invoked when task's progress property changes.
	 */
    public void propertyChange(PropertyChangeEvent evt) {
        if("progress" == evt.getPropertyName()) {
        	if(!isCancelled()){
        		int progress = (Integer) evt.getNewValue();
                if(!percentProgressBarStarted && progress<100){
                	Principal.getInstance().startPercentProgressBar();
                	percentProgressBarStarted=true;
                }
                Principal.getInstance().setProgressBarValue(progress);
        	}
        } 
    }
    
    public void cancelInterruptIfRunning(){
    	cancel(true);
    	sleepForInterrupt = true;
    }
    
    public long getMaximum() {
		return maximum;
	}
    
    public long getValue() {
		return value;
	}
}
