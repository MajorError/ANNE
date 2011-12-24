package uk.ac.ic.doc.neuralnets.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import sun.reflect.FieldAccessor;
import uk.ac.ic.doc.neuralnets.expressions.ExpressionException;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpression;
import uk.ac.ic.doc.neuralnets.expressions.ast.ASTExpressionFactory;

/**
 * 
 * @author Peter Coetzee
 */
public class MethodPseudoAccessor implements FieldAccessor {

	private Logger log = Logger.getLogger(MethodPseudoAccessor.class);
	private Method get, set;
	private Class expected;

	public MethodPseudoAccessor(Field f) throws NoSuchMethodException {
		this(f.getDeclaringClass(), f.getName());
	}

	public MethodPseudoAccessor(Class<?> c, String f)
			throws NoSuchMethodException {
		if (f.startsWith("get") || f.startsWith("set"))
			f = f.substring(3);
		f = f.substring(0, 1).toUpperCase() + f.substring(1);
		try {
			get = c.getMethod("get" + f);
		} catch (NoSuchMethodException ex) {
			log.trace("No method for getting " + f, ex);
		}
		try {
			set = c.getMethod("set" + f, Double.class);
			expected = Double.class;
		} catch (NoSuchMethodException ex) {
			try {
				set = c.getMethod("set" + f, double.class);
				expected = double.class;
			} catch (NoSuchMethodException ex2) {
				try {
					set = c.getMethod("set" + f, int.class);
					expected = int.class;
				} catch (NoSuchMethodException ex3) {
					try {
						set = c.getMethod("set" + f, ASTExpression.class);
						expected = ASTExpression.class;
					} catch (NoSuchMethodException ex4) {
						try {
							set = c.getMethod("set" + f, String.class);
							expected = String.class;
						} catch (NoSuchMethodException ex5) {
							log.trace("No method for setting " + f, ex);
						}
					}
				}
			}
		}
	}

	public Object get(Object o) throws IllegalArgumentException {
		try {
			return get.invoke(o);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public boolean getBoolean(Object o) throws IllegalArgumentException {
		return Boolean.parseBoolean(get(o).toString());
	}

	public byte getByte(Object o) throws IllegalArgumentException {
		return Byte.parseByte(get(o).toString());
	}

	public char getChar(Object o) throws IllegalArgumentException {
		return get(o).toString().charAt(0);
	}

	public short getShort(Object o) throws IllegalArgumentException {
		return Short.decode(get(o).toString());
	}

	public int getInt(Object o) throws IllegalArgumentException {
		return Integer.parseInt(get(o).toString());
	}

	public long getLong(Object o) throws IllegalArgumentException {
		return Long.parseLong(get(o).toString());
	}

	public float getFloat(Object o) throws IllegalArgumentException {
		return Float.parseFloat(get(o).toString());
	}

	public double getDouble(Object o) throws IllegalArgumentException {
		return Double.parseDouble(get(o).toString());
	}

	public void set(Object o, Object v) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			if (!v.getClass().equals(expected.getClass()))
				if (expected.equals(String.class))
					v = v.toString();
				else if (expected.equals(ASTExpression.class))
					v = ASTExpressionFactory.get().getExpression(v.toString());
				else if (expected.equals(Double.class))
					v = Double.valueOf(v.toString());
				else if (expected.equals(double.class))
					v = Double.parseDouble(v.toString());
				else if (expected.equals(int.class))
					v = Integer.parseInt(v.toString());
			// log.trace( "Setting " + set.getName() + " with " + v +
			// ", casting "
			// + v.getClass() + " to " + expected );
			set.invoke(o, v); // expected.cast( v ) );
		} catch (ExpressionException ex) {
			throw new IllegalArgumentException(ex);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setBoolean(Object o, boolean b)
			throws IllegalArgumentException, IllegalAccessException {
		try {
			set.invoke(o, b);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setByte(Object o, byte b) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, b);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setChar(Object o, char c) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, c);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setShort(Object o, short s) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, s);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setInt(Object o, int i) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, i);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setLong(Object o, long l) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, l);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setFloat(Object o, float f) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, f);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public void setDouble(Object o, double d) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			set.invoke(o, d);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

}
