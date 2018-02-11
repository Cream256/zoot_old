package com.zootcat.controllers.physics;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zootcat.controllers.factory.ControllerAnnotations;
import com.zootcat.scene.ZootActor;
import com.zootcat.utils.BitMaskConverter;

public class OnCollideControllerTest
{
	protected int enterCount = 0;
	protected int leaveCount = 0;
	protected OnCollideController ctrl;
	protected Filter ctrlActorFilter;
	protected Filter otherActorFilter;
	@Mock protected ZootActor ctrlActor;
	@Mock protected ZootActor otherActor;
	@Mock protected Fixture ctrlActorFixture;
	@Mock protected Fixture otherActorFixture;
	@Mock protected Contact contact;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		enterCount = 0;
		leaveCount = 0;
		ctrlActorFilter = new Filter();
		otherActorFilter = new Filter();		
		ctrl = new OnCollideController(){
			@Override
			public void onEnter(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++enterCount;
			}

			@Override
			public void onLeave(ZootActor actorA, ZootActor actorB, Contact contact)
			{
				++leaveCount;
			}};
						
		when(contact.getFixtureA()).thenReturn(ctrlActorFixture);
		when(contact.getFixtureB()).thenReturn(otherActorFixture);
		when(ctrlActorFixture.getFilterData()).thenReturn(ctrlActorFilter);
		when(otherActorFixture.getFilterData()).thenReturn(otherActorFilter);
	}
	
	@Test
	public void getControllerActorTest()
	{
		//when
		ctrl.init(ctrlActor);
		
		//then
		assertEquals(ctrlActor, ctrl.getControllerActor());
	}
	
	@Test
	public void beginContactDefaultCategoryShouldMatchAllCollisionsTest()
	{
		//given
		ctrlActorFilter.categoryBits = 1;
		otherActorFilter.categoryBits = 2;		
		ctrl.init(ctrlActor);
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);		
		assertEquals("Should match collision", 1, enterCount);

		//then
		ctrl.beginContact(otherActor, ctrlActor, contact);		
		assertEquals("Should match collision", 2, enterCount);
	}
	
	@Test
	public void beginContactCategoriesTest()
	{
		//given
		short cat1 = BitMaskConverter.Instance.fromString("CAT1");
		short cat2 = BitMaskConverter.Instance.fromString("CAT2");
		
		//when
		ctrlActorFilter.categoryBits = cat1;
		otherActorFilter.categoryBits = cat2;
		ControllerAnnotations.setControllerParameter(ctrl, "category", "CAT2");
		ctrl.init(ctrlActor);
				
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);		
		assertEquals("Should match collision", 1, enterCount);
		
		//when
		otherActorFilter.categoryBits = cat1;

		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);		
		assertEquals("Should not match collision", 1, enterCount);
	}
	
	@Test
	public void endContactDefaultCategoryShouldMatchAllCollisionsTest()
	{
		//given
		ctrlActorFilter.categoryBits = 1;
		otherActorFilter.categoryBits = 2;		
		ctrl.init(ctrlActor);
		
		//then
		ctrl.endContact(ctrlActor, otherActor, contact);		
		assertEquals("Should match collision", 1, leaveCount);

		//then
		ctrl.endContact(otherActor, ctrlActor, contact);		
		assertEquals("Should match collision", 2, leaveCount);		
	}
	
	@Test
	public void endContactCategoriesTest()
	{
		//given
		short cat1 = BitMaskConverter.Instance.fromString("CAT1");
		short cat2 = BitMaskConverter.Instance.fromString("CAT2");
		
		//when
		ctrlActorFilter.categoryBits = cat1;
		otherActorFilter.categoryBits = cat2;
		ControllerAnnotations.setControllerParameter(ctrl, "category", "CAT2");		
		ctrl.init(ctrlActor);
		
		//then
		ctrl.endContact(ctrlActor, otherActor, contact);		
		assertEquals("Should match collision", 1, leaveCount);

		//then
		otherActorFilter.categoryBits = cat1;
		ctrl.endContact(ctrlActor, otherActor, contact);		
		assertEquals("Should not match collision", 1, leaveCount);		
	}
	
	@Test
	public void preSolveTest()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		Contact contact =  mock(Contact.class);
		Manifold manifold = mock(Manifold.class);
					
		ctrl.preSolve(actorA, actorB, contact, manifold);
		verifyZeroInteractions(actorA, actorB, contact, manifold);
	}
	
	@Test
	public void postSolveTest()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		ContactImpulse contactImpulse = mock(ContactImpulse.class);
				
		ctrl.postSolve(actorA, actorB, contactImpulse);
		verifyZeroInteractions(actorA, actorB, contactImpulse);
	}
}
