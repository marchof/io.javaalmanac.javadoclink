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

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

final class MethodParameterStrategy {

	private String begin;
	private String separator;
	private String end;
	private String array;

	MethodParameterStrategy(String begin, String separator, String end, String array) {
		this.begin = begin;
		this.separator = separator;
		this.end = end;
		this.array = array;
	}

	String fromDesc(String desc, boolean vararg) {
		var result = new ArrayList<Param>();
		var chars = desc.substring(1, desc.indexOf(')')).chars().iterator();
		while (chars.hasNext()) {
			result.add(nextType(chars, desc));
		}
		return join(result, vararg);
	}

	private Param nextType(PrimitiveIterator.OfInt chars, String desc) {
		switch (chars.nextInt()) {
		case 'Z':
			return new Param("boolean");
		case 'C':
			return new Param("char");
		case 'B':
			return new Param("byte");
		case 'S':
			return new Param("short");
		case 'I':
			return new Param("int");
		case 'F':
			return new Param("float");
		case 'J':
			return new Param("long");
		case 'D':
			return new Param("double");
		case 'L':
			var sb = new StringBuilder();
			for (var c = chars.nextInt(); c != ';'; c = chars.nextInt()) {
				sb.append((char) c);
			}
			return new Param(sb.toString().replace('/', '.').replace('$', '.'));
		case '[':
			return nextType(chars, desc).addDimension();
		default:
			throw new IllegalArgumentException("Invalid method descriptor: " + desc);
		}
	}

	String fromClasses(Class<?>[] params, boolean vararg) {
		var result = new ArrayList<Param>();
		for (Class<?> param : params) {
			result.add(fromClass(param));
		}
		return join(result, vararg);
	}

	private Param fromClass(Class<?> param) {
		if (param.isArray()) {
			return fromClass(param.getComponentType()).addDimension();
		} else {
			return new Param(param.getName().replace('$', '.'));
		}
	}

	private String join(List<Param> params, boolean vararg) {
		var sb = new StringBuilder(begin);
		for (var idx = 0; idx < params.size(); idx++) {
			if (idx > 0) {
				sb.append(separator);
			}
			// vararg syntyx only applies for the last parameter:
			sb.append(params.get(idx).toString(vararg && (idx == params.size() - 1)));
		}
		return sb.append(end).toString();
	}

	private class Param {

		private final String type;
		private final int dimensions;

		Param(String type, int dimensions) {
			this.type = type;
			this.dimensions = dimensions;
		}

		Param(String type) {
			this(type, 0);
		}

		Param addDimension() {
			return new Param(type, dimensions + 1);
		}

		String toString(boolean vararg) {
			if (dimensions > 0) {
				var s = new Param(type, dimensions - 1).toString(false);
				return s + (vararg ? "..." : array);
			}
			return type;
		}
	}

}
