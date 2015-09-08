package com.dm.util;

import java.util.UUID;
import java.util.Vector;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sequencing.Sequence;
import org.eclipse.persistence.sessions.Session;

public class UUIDGenerator extends Sequence implements SessionCustomizer {

	private static final long serialVersionUID = 5946882855151793128L;

	public UUIDGenerator() {
		super();
	}

	public UUIDGenerator(String name) {
		super(name);
	}

	public void customize(Session session) throws Exception {
		UUIDGenerator sequence = new UUIDGenerator("uuid");
		session.getLogin().addSequence(sequence);
	}

	@Override
	public boolean shouldAcquireValueAfterInsert() {
		return false;
	}

	@Override
	public boolean shouldUseTransaction() {
		return false;
	}
	
	@Override
	public boolean shouldUsePreallocation() {
		return false;
	}

	@Override
	public Object getGeneratedValue(Accessor accessor, AbstractSession writeSession, String seqName) {
		return UUID.randomUUID().toString();
	}

	@Override
	public Vector getGeneratedVector(Accessor accessor, AbstractSession writeSession, String seqName, int size) {
		return null;
	}

	@Override
	public void onConnect() {

	}

	@Override
	public void onDisconnect() {

	}

}