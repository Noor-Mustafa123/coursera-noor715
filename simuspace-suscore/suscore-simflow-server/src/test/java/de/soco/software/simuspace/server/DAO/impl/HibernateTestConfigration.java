package de.soco.software.simuspace.server.DAO.impl;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.query.Query;

/**
 * Generic Class which will be used for <br> Junit testing of Dao Clases
 *
 * @author Nosheen.Sharif
 */
public class HibernateTestConfigration {

    private static IMocksControl mockControl = EasyMock.createControl();

    private static Session session;

    private static Query query;

    private static SessionFactory sessionFactory;

    private static Transaction transaction;

    private static Criteria criteria;

    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    public static void setUp() throws Exception {
        mockControl().resetToNice();
        session = mockControl().createMock( Session.class );
        sessionFactory = mockControl().createMock( SessionFactory.class );
        query = mockControl().createMock( Query.class );
        transaction = mockControl().createMock( Transaction.class );
        criteria = mockControl().createMock( Criteria.class );
        EasyMock.expect( session.getSessionFactory() ).andReturn( sessionFactory ).anyTimes();
        EasyMock.expect( session.createQuery( EasyMock.anyString() ) ).andReturn( query ).anyTimes();
        EasyMock.expect( session.beginTransaction() ).andReturn( transaction ).anyTimes();
        EasyMock.expect( session.getTransaction() ).andReturn( transaction ).anyTimes();

    }

    /**
     * Prepare and return criteria.
     *
     * @param clazz
     *         the clazz
     *
     * @return the criteria
     */
    public static Criteria prepareAndReturnCriteria( Class clazz ) {
        EasyMock.expect( session.createCriteria( clazz ) ).andReturn( criteria ).anyTimes();
        EasyMock.expect( criteria.add( ( Criterion ) EasyMock.anyObject() ) ).andReturn( criteria ).anyTimes();
        EasyMock.expect( criteria.setProjection( ( Projection ) EasyMock.anyObject() ) ).andReturn( criteria ).anyTimes();
        EasyMock.expect( criteria.addOrder( Order.asc( EasyMock.anyString() ) ) ).andReturn( criteria ).anyTimes();
        return criteria;
    }

    /**
     * Gets the query.
     *
     * @return the query
     */
    public static Query getQuery() {
        return query;
    }

    /**
     * Gets the session.
     *
     * @return the session
     */
    public static Session getSession() {
        return session;
    }

}
