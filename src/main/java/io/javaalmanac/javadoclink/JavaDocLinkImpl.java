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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

final class JavaDocLinkImpl implements JavaDocLink {

	static final Map<String, JavaDocLinkImpl> VERSIONS = new LinkedHashMap<>();

	static {
		VERSIONS.put("1.1", new JavaDocLinkImpl("", //
				new MethodParameterStrategy("(", ", ", ")", "%5B%5D"), //
				JavaDocLinkImpl::moduleLinkStrategy1_1, //
				JavaDocLinkImpl::packageLinkStrategy1_1, //
				JavaDocLinkImpl::classLinkStrategy1_1, //
				JavaDocLinkImpl::constructorNameStrategy1_1));

		VERSIONS.put("1.2", new JavaDocLinkImpl("", //
				new MethodParameterStrategy("(", ", ", ")", "%5B%5D"), //
				JavaDocLinkImpl::moduleLinkStrategy1_1, //
				JavaDocLinkImpl::packageLinkStrategy1_2, //
				JavaDocLinkImpl::classLinkStrategy1_2, //
				JavaDocLinkImpl::constructorNameStrategy1_1));

		VERSIONS.put("1.3", VERSIONS.get("1.2"));
		VERSIONS.put("1.4", VERSIONS.get("1.2"));
		VERSIONS.put("5", VERSIONS.get("1.2"));
		VERSIONS.put("6", VERSIONS.get("1.2"));
		VERSIONS.put("7", VERSIONS.get("1.2"));

		VERSIONS.put("8", new JavaDocLinkImpl("", //
				new MethodParameterStrategy("-", "-", "-", ":A"), //
				JavaDocLinkImpl::moduleLinkStrategy1_1, //
				JavaDocLinkImpl::packageLinkStrategy1_2, //
				JavaDocLinkImpl::classLinkStrategy1_2, //
				JavaDocLinkImpl::constructorNameStrategy8));

		VERSIONS.put("9", new JavaDocLinkImpl("", //
				new MethodParameterStrategy("-", "-", "-", ":A"), //
				JavaDocLinkImpl::moduleLinkStrategy9, //
				JavaDocLinkImpl::packageLinkStrategy1_2, //
				JavaDocLinkImpl::classLinkStrategy1_2, //
				JavaDocLinkImpl::constructorNameStrategy8));

		VERSIONS.put("10", new JavaDocLinkImpl("", //
				new MethodParameterStrategy("(", ",", ")", "%5B%5D"), //
				JavaDocLinkImpl::moduleLinkStrategy9, //
				JavaDocLinkImpl::packageLinkStrategy1_2, //
				JavaDocLinkImpl::classLinkStrategy1_2, //
				JavaDocLinkImpl::constructorNameStrategy10));

		VERSIONS.put("11", new JavaDocLinkImpl("", //
				new MethodParameterStrategy("(", ",", ")", "%5B%5D"), //
				JavaDocLinkImpl::moduleLinkStrategy11, //
				JavaDocLinkImpl::packageLinkStrategy11, //
				JavaDocLinkImpl::classLinkStrategy11, //
				JavaDocLinkImpl::constructorNameStrategy10));

		VERSIONS.put("12", VERSIONS.get("11"));
		VERSIONS.put("13", VERSIONS.get("11"));
		VERSIONS.put("14", VERSIONS.get("11"));
		VERSIONS.put("15", VERSIONS.get("11"));
		VERSIONS.put("16", VERSIONS.get("11"));
		VERSIONS.put("17", VERSIONS.get("11"));
		VERSIONS.put("18", VERSIONS.get("11"));
	}

	private final String base;
	private final MethodParameterStrategy parameterStrategy;
	private final Function<String, String> moduleLinkStrategy;
	private final BiFunction<String, String, String> packageLinkStrategy;
	private final BiFunction<String, String, String> classLinkStrategy;
	private final Function<String, String> constructorNameStrategy;

	private JavaDocLinkImpl(String base, MethodParameterStrategy parameters,
			Function<String, String> moduleLinkStrategy, BiFunction<String, String, String> packageLinkStrategy,
			BiFunction<String, String, String> classLinkStrategy, Function<String, String> constructorNameStrategy) {
		if (!base.isEmpty() && !base.endsWith("/")) {
			this.base = base + "/";
		} else {
			this.base = base;
		}
		this.parameterStrategy = parameters;
		this.moduleLinkStrategy = moduleLinkStrategy;
		this.packageLinkStrategy = packageLinkStrategy;
		this.classLinkStrategy = classLinkStrategy;
		this.constructorNameStrategy = constructorNameStrategy;
	}

	@Override
	public JavaDocLink withBaseUrl(String baseurl) {
		return new JavaDocLinkImpl(baseurl, parameterStrategy, moduleLinkStrategy, packageLinkStrategy,
				classLinkStrategy, constructorNameStrategy);
	}

	@Override
	public String moduleLink(String modulename) {
		return base + moduleLinkStrategy.apply(modulename);
	}

	@Override
	public String moduleLink(Module module) {
		return moduleLink(module.getName());
	}

	@Override
	public String packageLink(String modulename, String packagename) {
		return base + packageLinkStrategy.apply(modulename, packagename);
	}

	@Override
	public String packageLink(Module module, Package pkg) {
		return packageLink(module.getName(), internalName(pkg.getName()));
	}

	@Override
	public String classLink(String modulename, String classname) {
		return base + classLinkStrategy.apply(modulename, classname);
	}

	@Override
	public String classLink(Class<?> cls) {
		return classLink(cls.getModule().getName(), internalName(cls));
	}

	private String methodLink0(String modulename, String classname, String methodname, String params) {
		var name = "<init>".equals(methodname) ? constructorNameStrategy.apply(classname) : methodname;
		return classLink(modulename, classname) + "#" + name + params;
	}

	@Override
	public String methodLink(String modulename, String classname, String methodname, String desc, boolean vararg) {
		return methodLink0(modulename, classname, methodname, parameterStrategy.fromDesc(desc, vararg));
	}

	@Override
	public String methodLink(Executable executable) {
		var owner = executable.getDeclaringClass();
		var name = executable instanceof Constructor ? "<init>" : executable.getName();
		return methodLink0(owner.getModule().getName(), internalName(owner), name,
				parameterStrategy.fromClasses(executable.getParameterTypes(), executable.isVarArgs()));
	}

	@Override
	public String fieldLink(String modulename, String classname, String fieldname) {
		return classLink(modulename, classname) + "#" + fieldname;
	}

	@Override
	public String fieldLink(Field field) {
		return classLink(field.getDeclaringClass()) + "#" + field.getName();
	}

	private static String internalName(Class<?> cls) {
		return internalName(cls.getName());
	}

	private static String internalName(String srcname) {
		return srcname.replace('.', '/');
	}

	private static String moduleLinkStrategy1_1(String modulename) {
		throw new UnsupportedOperationException("Modules not supported before Java 9.");
	}

	private static String moduleLinkStrategy9(String modulename) {
		return modulename + "-summary.html";
	}

	private static String moduleLinkStrategy11(String modulename) {
		return modulename + "/module-summary.html";
	}

	private static String packageLinkStrategy1_1(String modulename, String packagename) {
		return "Package-" + packagename.replace('/', '.') + ".html";
	}

	private static String packageLinkStrategy1_2(String modulename, String packagename) {
		return packagename + "/package-summary.html";
	}

	private static String packageLinkStrategy11(String modulename, String packagename) {
		return modulename + "/" + packagename + "/package-summary.html";
	}

	private static String classLinkStrategy1_1(String modulename, String classname) {
		return classname.replace('/', '.') + ".html";
	}

	private static String classLinkStrategy1_2(String modulename, String classname) {
		return classname.replace('$', '.') + ".html";
	}

	private static String classLinkStrategy11(String modulename, String classname) {
		return modulename + "/" + classname.replace('$', '.') + ".html";
	}

	private static String constructorNameStrategy1_1(String owner) {
		var sep = owner.lastIndexOf('/');
		return (sep == -1 ? owner : owner.substring(sep + 1)).replace('$', '.');
	}

	private static String constructorNameStrategy8(String owner) {
		var sep = Math.max(owner.lastIndexOf('/'), owner.lastIndexOf('$'));
		return sep == -1 ? owner : owner.substring(sep + 1);
	}

	private static String constructorNameStrategy10(String owner) {
		return "%3Cinit%3E";
	}

}
