package org.bsc.maven.reporting.sink;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.swing.text.AttributeSet;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributeSet;
import org.apache.maven.doxia.sink.SinkEventAttributes;

public class SinkDelegate implements InvocationHandler
	    {
	        private final Sink sink;

	        public SinkDelegate( Sink sink ) {
	            this.sink = sink;
	        }
	
	        /** {@inheritDoc} */
	        public Object invoke( Object proxy, Method method, Object[] args )
	            throws Throwable
	        {
	            Class<?>[] parameterTypes = method.getParameterTypes();
	
	            for ( int i = parameterTypes.length - 1; i >= 0; i-- )
	            {
	                if ( AttributeSet.class.isAssignableFrom( parameterTypes[i] ) )
	                {
	                    parameterTypes[i] = SinkEventAttributes.class;
	                }
	            }
	
	            if ( args != null )
	            {
	                for ( int i = args.length - 1; i >= 0; i-- )
	                {
	                    if ( AttributeSet.class.isInstance( args[i] ) )
	                    {
	                        args[i] = new SinkEventAttributeSet( (AttributeSet) args[i] );
	                    }
	                }
	            }
	
	            Method target = Sink.class.getMethod( method.getName(), parameterTypes );
	
	            return target.invoke( sink, args );
	        }
	    }