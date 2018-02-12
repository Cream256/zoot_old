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
		
		BitMaskConverter.Instance.clear();
	}
	
	@Test
	public void shouldReturnControllerActor()
	{
		//when
		ctrl.init(ctrlActor);
		
		//then
		assertEquals(ctrlActor, ctrl.getControllerActor());
	}
	
	@Test
	public void shouldCollideWithAllWhenMaskIsNotGiven()
	{
		//given
		ctrlActorFilter.categoryBits = 1;
		otherActorFilter.categoryBits = 2;
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "ABC");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", null);
		ctrl.init(ctrlActor);
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);		
		assertEquals("Should match collision", 1, enterCount);

		//then
		ctrl.beginContact(otherActor, ctrlActor, contact);		
		assertEquals("Should match collision", 2, enterCount);
	}
	
	@Test
	public void shouldCollideWithCategoriesInMaskTest()
	{
		//given
		final short categoryA = BitMaskConverter.Instance.convertMask("A");
		final short categoryB = BitMaskConverter.Instance.convertMask("B");
		final short categoryC = BitMaskConverter.Instance.convertMask("C");
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "A");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "B | C");
		ctrl.init(ctrlActor);
		
		//when
		ctrlActorFilter.categoryBits = categoryA;
		otherActorFilter.categoryBits = categoryB;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should collide on begin contact", 1, enterCount);
		assertEquals("Should collide on end contact", 1, leaveCount);
		
		//when
		otherActorFilter.categoryBits = categoryC;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should collide on begin contact", 2, enterCount);
		assertEquals("Should collide on end contact", 2, leaveCount);
	}
	
	@Test
	public void shouldNotCollideWithCategoriesNotInMaskTest()
	{
		//given
		final short categoryA = BitMaskConverter.Instance.convertMask("A");
		final short categoryB = BitMaskConverter.Instance.convertMask("B");
		final short categoryC = BitMaskConverter.Instance.convertMask("C");
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "A");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "D | F");
		ctrl.init(ctrlActor);
		
		//when
		ctrlActorFilter.categoryBits = categoryA;
		otherActorFilter.categoryBits = categoryA;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
		
		//when
		otherActorFilter.categoryBits = categoryB;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
		
		//when
		otherActorFilter.categoryBits = categoryC;
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
	}
	
	@Test
	public void shouldNotCollideIfOtherFixtureDoesNotMaskActorCategoryTest()
	{
		//given
		final short categoryA = BitMaskConverter.Instance.convertMask("A");
		final short categoryB = BitMaskConverter.Instance.convertMask("B");
		
		ControllerAnnotations.setControllerParameter(ctrl, "category", "A");
		ControllerAnnotations.setControllerParameter(ctrl, "mask", "B");
		ctrl.init(ctrlActor);
		
		//when
		ctrlActorFilter.categoryBits = categoryA;		
		otherActorFilter.categoryBits = categoryB;
		otherActorFilter.maskBits = BitMaskConverter.Instance.convertMask("B | C");
		
		//then
		ctrl.beginContact(ctrlActor, otherActor, contact);
		ctrl.endContact(ctrlActor, otherActor, contact);
		assertEquals("Should not collide on begin contact", 0, enterCount);
		assertEquals("Should not collide on end contact", 0, leaveCount);
	}
				
	@Test
	public void shouldDoNothingOnPreSolveTest()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		Contact contact =  mock(Contact.class);
		Manifold manifold = mock(Manifold.class);
					
		ctrl.preSolve(actorA, actorB, contact, manifold);
		verifyZeroInteractions(actorA, actorB, contact, manifold);
	}
	
	@Test
	public void shouldDoNothingOnPostSolveTest()
	{
		ZootActor actorA = mock(ZootActor.class);
		ZootActor actorB = mock(ZootActor.class);
		ContactImpulse contactImpulse = mock(ContactImpulse.class);
				
		ctrl.postSolve(actorA, actorB, contactImpulse);
		verifyZeroInteractions(actorA, actorB, contactImpulse);
	}
}
