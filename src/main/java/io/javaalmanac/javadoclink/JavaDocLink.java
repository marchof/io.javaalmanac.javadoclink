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

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * API to create Javadoc deep links for various Java language elements. Use
 * {@link #forVersion(String)} to get a instance for a specific Java version.
 */
public interface JavaDocLink {

	/**
	 * Returns a list of all currently supported Java versions
	 * 
	 * @return currently supported Java versions
	 */
	public static List<String> supportedVersions() {
		return List.copyOf(JavaDocLinkImpl.VERSIONS.keySet());
	}

	/**
	 * Request a instance for the given Java version. As the output structure of the
	 * Javadoc tool changed over the time it is important to generate links specific
	 * for the JDK version which was used to generate the Javadoc tree.
	 * 
	 * @param majorversion JDK version used to generate the Javadoc tree
	 * @return corresponding {@link JavaDocLink} instance
	 */
	public static JavaDocLink forVersion(String majorversion) {
		return JavaDocLinkImpl.VERSIONS.get(majorversion);
	}

	/**
	 * By default relative URLs are generated. With this method the base URL can be
	 * set.
	 * 
	 * @param baseurl URL which should be used to create Javadoc links relative to
	 * @return new {@link JavaDocLink} instance working with the given base URL
	 */
	JavaDocLink withBaseUrl(String baseurl);

	/**
	 * Creates a link to the module overview page for a module name (e.g.
	 * <code>java.base</code>). This operation is only supported since Java 9.
	 * 
	 * @param modulename name in dot notation
	 * @return link to module overview page
	 */
	String moduleLink(String modulename);

	/**
	 * Creates a link to the module overview page for a {@link Module} instance.
	 * This operation is only supported since Java 9.
	 * 
	 * @param module module instance
	 * @return link to module overview page
	 */
	String moduleLink(Module module);

	/**
	 * Create a link to the package overview page for the given module and package
	 * name (e.g. <code>java/lang</code>).
	 * 
	 * @param modulename  name in dot notation
	 * @param packagename name in JVM internal notation
	 * @return link to package overview page
	 */
	String packageLink(String modulename, String packagename);

	/**
	 * Create a link to the package overview page for the given {@link Module} and
	 * {@link Package} instance.
	 * 
	 * @param module module instance
	 * @param pkg    package instance
	 * @return link to package overview page
	 */
	String packageLink(Module module, Package pkg);

	/**
	 * Creates a link to the class page for the given module and class (e.g.
	 * <code>java/lang/String</code>).
	 * 
	 * @param modulename name in dot notation
	 * @param classname  name in JVM internal notation
	 * @return link to the class page
	 */
	String classLink(String modulename, String classname);

	/**
	 * Creates a link to the class page for the given {@link Class} instance.
	 * 
	 * @param cls class instance
	 * @return link to the class page
	 */
	String classLink(Class<?> cls);

	/**
	 * Creates a link to the class page for the given module, class and method name.
	 * 
	 * @param modulename name in dot notation
	 * @param classname  name in JVM internal notation
	 * @param methodname name of the method
	 * @param desc       JVM internal signature descriptor
	 * @param vararg     <code>true</code>, if the last array parameter is used as
	 *                   vararg parameter
	 * @return link to the class page
	 */
	String methodLink(String modulename, String classname, String methodname, String desc, boolean vararg);

	/**
	 * Creates a link to a method within a class page the given {@link Method} oder
	 * {@link Constructor} instance.
	 * 
	 * @param executable method or constructor instance
	 * @return link to the method within the corresponding class page
	 */
	String methodLink(Executable executable);

	/**
	 * Creates a link to a field within a class page the given module, class and
	 * field name.
	 * 
	 * @param modulename name in dot notation
	 * @param classname  name in JVM internal notation
	 * @param fieldname  field name
	 * @return link to the field within the corresponding class page
	 */
	String fieldLink(String modulename, String classname, String fieldname);

	/**
	 * Creates a link to a field within a class page the given {@link Field}
	 * instance.
	 * 
	 * @param field field instance
	 * @return link to the field within the corresponding class page
	 */
	String fieldLink(Field field);

}
