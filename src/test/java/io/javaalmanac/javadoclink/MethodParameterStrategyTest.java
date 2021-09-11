/*******************************************************************************
 * Copyright (c) 2021 Mountainminds GmbH & Co. KG
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 *******************************************************************************/
package io.javaalmanac.javadoclink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MethodParameterStrategyTest {

	private MethodParameterStrategy parameters;

	@BeforeEach
	public void setup() {
		parameters = new MethodParameterStrategy("(", ",", ")", "[]");
	}

	@Test
	public void should_convert_primitive_types_from_desc() {
		assertEquals("(boolean,byte,char,short,int,long,float,double)", parameters.fromDesc("(ZBCSIJFD)V", false));
	}

	@Test
	public void should_convert_primitive_types_from_classes() throws Exception {
		var classes = new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
				Float.TYPE, Double.TYPE };
		assertEquals("(boolean,byte,char,short,int,long,float,double)", parameters.fromClasses(classes, false));
	}

	@Test
	public void should_convert_object_types_from_desc() {
		assertEquals("(java.util.Map.Entry)", parameters.fromDesc("(Ljava/util/Map$Entry;)V", false));
	}

	@Test
	public void should_convert_object_types_from_classes() {
		assertEquals("(java.util.Map.Entry)", parameters.fromClasses(new Class[] { Map.Entry.class }, false));
	}

	@Test
	public void should_convert_arrays_types_from_desc() {
		assertEquals("(java.lang.String[][])", parameters.fromDesc("([[Ljava/lang/String;)V", false));
	}

	@Test
	public void should_convert_arrays_types_from_classes() {
		assertEquals("(java.lang.String[][])", parameters.fromClasses(new Class[] { String[][].class }, false));
	}

	@Test
	public void should_convert_varargs_from_desc() {
		assertEquals("(java.lang.String[],java.lang.String...)",
				parameters.fromDesc("([Ljava/lang/String;[Ljava/lang/String;)V", true));
	}

	@Test
	public void should_convert_varargs_from_classes() {
		assertEquals("(java.lang.String[],java.lang.String...)",
				parameters.fromClasses(new Class[] { String[].class, String[].class }, true));
	}

	@Test
	public void should_throw_exception_with_full_signature_when_unknown_type_is_provided() {
		var ex = assertThrows(IllegalArgumentException.class, () -> parameters.fromDesc("(XYZ)V", false));
		assertEquals("Invalid method descriptor: (XYZ)V", ex.getMessage());
	}
}
