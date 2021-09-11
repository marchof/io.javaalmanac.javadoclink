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

abstract class JavaDocLinkImpl implements JavaDocLink {

	static final Map<String, JavaDocLinkImpl> VERSIONS = new LinkedHashMap<>();

	static {
		VERSIONS.put("1.1", new Java1_1(""));
		VERSIONS.put("1.2", new Java1_2(""));
		VERSIONS.put("1.3", new Java1_2(""));
		VERSIONS.put("1.4", new Java1_2(""));
		VERSIONS.put("5", new Java1_2(""));
		VERSIONS.put("6", new Java1_2(""));
		VERSIONS.put("7", new Java1_2(""));
		VERSIONS.put("8", new Java8(""));
		VERSIONS.put("9", new Java9(""));
		VERSIONS.put("10", new Java10(""));
		VERSIONS.put("11", new Java11(""));
		VERSIONS.put("12", new Java11(""));
		VERSIONS.put("13", new Java11(""));
		VERSIONS.put("14", new Java11(""));
		VERSIONS.put("15", new Java11(""));
		VERSIONS.put("16", new Java11(""));
		VERSIONS.put("17", new Java11(""));
		VERSIONS.put("18", new Java11(""));
	}

	final String base;

	final MethodParameters parameters;

	JavaDocLinkImpl(String base, MethodParameters parameters) {
		if (!base.isEmpty() && !base.endsWith("/")) {
			this.base = base + "/";
		} else {
			this.base = base;
		}
		this.parameters = parameters;
	}

	@Override
	public String moduleLink(Module module) {
		return moduleLink(module.getName());
	}

	@Override
	public String packageLink(Module module, Package pkg) {
		return packageLink(module.getName(), internalName(pkg));
	}

	@Override
	public String classLink(Class<?> cls) {
		return classLink(cls.getModule().getName(), internalName(cls));
	}

	private String methodLink0(String modulename, String classname, String methodname, String params) {
		var name = "<init>".equals(methodname) ? constructorName(classname) : methodname;
		return classLink(modulename, classname) + "#" + name + params;
	}

	abstract String constructorName(String owner);

	@Override
	public String methodLink(String modulename, String classname, String methodname, String desc, boolean vararg) {
		return methodLink0(modulename, classname, methodname, parameters.fromDesc(desc, vararg));
	}

	@Override
	public String methodLink(Executable executable) {
		var owner = executable.getDeclaringClass();
		var name = executable instanceof Constructor ? "<init>" : executable.getName();
		return methodLink0(owner.getModule().getName(), internalName(owner), name,
				parameters.fromClasses(executable.getParameterTypes(), executable.isVarArgs()));
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

	private static String internalName(Package pkg) {
		return internalName(pkg.getName());
	}

	private static String internalName(String srcname) {
		return srcname.replace('.', '/');
	}

	private static class Java1_1 extends JavaDocLinkImpl {

		Java1_1(String base) {
			super(base, new MethodParameters("(", ", ", ")", "%5B%5D"));
		}

		@Override
		public JavaDocLinkImpl withBaseUrl(String baseurl) {
			return new Java1_1(baseurl);
		}

		@Override
		public String moduleLink(String modulename) {
			throw new UnsupportedOperationException("Modules not supported before Java 9.");
		}

		@Override
		public String packageLink(String modulename, String packagename) {
			return base + "Package-" + packagename.replace('/', '.') + ".html";
		}

		@Override
		public String classLink(String modulename, String classname) {
			return base + classname.replace('/', '.') + ".html";
		}

		@Override
		String constructorName(String owner) {
			var sep = owner.lastIndexOf('/');
			return (sep == -1 ? owner : owner.substring(sep + 1)).replace('$', '.');
		}

	}

	private static class Java1_2 extends JavaDocLinkImpl {

		Java1_2(String base) {
			super(base, new MethodParameters("(", ", ", ")", "%5B%5D"));
		}

		@Override
		public JavaDocLinkImpl withBaseUrl(String baseurl) {
			return new Java1_2(baseurl);
		}

		@Override
		public String moduleLink(String modulename) {
			throw new UnsupportedOperationException("Modules not supported before Java 9.");
		}

		@Override
		public String packageLink(String modulename, String packagename) {
			return base + packagename + "/package-summary.html";
		}

		@Override
		public String classLink(String modulename, String classname) {
			return base + classname.replace('$', '.') + ".html";
		}

		@Override
		String constructorName(String owner) {
			var sep = owner.lastIndexOf('/');
			return (sep == -1 ? owner : owner.substring(sep + 1)).replace('$', '.');
		}
	}

	private static class Java8 extends JavaDocLinkImpl {

		Java8(String base) {
			super(base, new MethodParameters("-", "-", "-", ":A"));
		}

		@Override
		public JavaDocLinkImpl withBaseUrl(String baseurl) {
			return new Java8(baseurl);
		}

		@Override
		public String moduleLink(String modulename) {
			throw new UnsupportedOperationException("Modules not supported before Java 9.");
		}

		@Override
		public String packageLink(String modulename, String packagename) {
			return base + packagename + "/package-summary.html";
		}

		@Override
		public String classLink(String modulename, String classname) {
			return base + classname.replace('$', '.') + ".html";
		}

		@Override
		String constructorName(String owner) {
			var sep = Math.max(owner.lastIndexOf('/'), owner.lastIndexOf('$'));
			return sep == -1 ? owner : owner.substring(sep + 1);
		}
	}

	private static class Java9 extends JavaDocLinkImpl {

		Java9(String base) {
			super(base, new MethodParameters("-", "-", "-", ":A"));
		}

		@Override
		public JavaDocLinkImpl withBaseUrl(String baseurl) {
			return new Java9(baseurl);
		}

		@Override
		public String moduleLink(String modulename) {
			return base + modulename + "-summary.html";
		}

		@Override
		public String packageLink(String modulename, String packagename) {
			return base + packagename + "/package-summary.html";
		}

		@Override
		public String classLink(String modulename, String classname) {
			return base + classname.replace('$', '.') + ".html";
		}

		@Override
		String constructorName(String owner) {
			var sep = Math.max(owner.lastIndexOf('/'), owner.lastIndexOf('$'));
			return sep == -1 ? owner : owner.substring(sep + 1);
		}
	}

	private static class Java10 extends JavaDocLinkImpl {

		Java10(String base) {
			super(base, new MethodParameters("(", ",", ")", "%5B%5D"));
		}

		@Override
		public JavaDocLinkImpl withBaseUrl(String baseurl) {
			return new Java10(baseurl);
		}

		@Override
		public String moduleLink(String modulename) {
			return base + modulename + "-summary.html";
		}

		@Override
		public String packageLink(String modulename, String packagename) {
			return base + packagename + "/package-summary.html";
		}

		@Override
		public String classLink(String modulename, String classname) {
			return base + classname.replace('$', '.') + ".html";
		}

		@Override
		String constructorName(String owner) {
			return "%3Cinit%3E";
		}
	}

	private static class Java11 extends JavaDocLinkImpl {

		Java11(String base) {
			super(base, new MethodParameters("(", ",", ")", "%5B%5D"));
		}

		@Override
		public JavaDocLinkImpl withBaseUrl(String baseurl) {
			return new Java11(baseurl);
		}

		@Override
		public String moduleLink(String modulename) {
			return base + modulename + "/module-summary.html";
		}

		@Override
		public String packageLink(String modulename, String packagename) {
			return base + modulename + "/" + packagename + "/package-summary.html";
		}

		@Override
		public String classLink(String modulename, String classname) {
			return base + modulename + "/" + classname.replace('$', '.') + ".html";
		}

		@Override
		String constructorName(String owner) {
			return "%3Cinit%3E";
		}
	}

}
