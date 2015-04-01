/**
* Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
* Author: frank.garber@wellpoint.com
* 
*/

package com.wellpoint.mobility.aggregation.core.misc.pojo;

import com.wellpoint.mobility.aggregation.persistence.domain.OnDemandAction.ACTION;

/**
 * @author frank.garber@wellpoint.com
 *
 */
public class OnDemandActionPojo {

	protected Long id;

	protected String theKey;

	protected ACTION action;

	protected Boolean takeAction;

	protected String actionInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTheKey() {
		return theKey;
	}

	public void setTheKey(String theKey) {
		this.theKey = theKey;
	}

	public ACTION getAction() {
		return action;
	}

	public void setAction(ACTION action) {
		this.action = action;
	}

	public Boolean getTakeAction() {
		return takeAction;
	}

	public void setTakeAction(Boolean takeAction) {
		this.takeAction = takeAction;
	}

	public String getActionInfo() {
		return actionInfo;
	}

	public void setActionInfo(String actionInfo) {
		this.actionInfo = actionInfo;
	}

	@Override
	public String toString() {
		return "OnDemandActionPojo [id=" + id + ", theKey=" + theKey + ", action=" + action + ", takeAction=" + takeAction + ", actionInfo=" + actionInfo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((actionInfo == null) ? 0 : actionInfo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((takeAction == null) ? 0 : takeAction.hashCode());
		result = prime * result + ((theKey == null) ? 0 : theKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OnDemandActionPojo other = (OnDemandActionPojo) obj;
		if (action != other.action) {
			return false;
		}
		if (actionInfo == null) {
			if (other.actionInfo != null) {
				return false;
			}
		}
		else if (!actionInfo.equals(other.actionInfo)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}
		if (takeAction == null) {
			if (other.takeAction != null) {
				return false;
			}
		}
		else if (!takeAction.equals(other.takeAction)) {
			return false;
		}
		if (theKey == null) {
			if (other.theKey != null) {
				return false;
			}
		}
		else if (!theKey.equals(other.theKey)) {
			return false;
		}
		return true;
	}
	
	
} // of class
