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

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class MethodPrimitiveParametersTest extends JavaDocLinkTest {

	@Override
	protected String createLink(JavaDocLink jdl) {
		return jdl.methodLink("java.base", "java/lang/Math", "max", "(DD)V", false);
	}

	@Override
	protected String createLinkReflective(JavaDocLink jdl) throws Exception {
		return jdl.methodLink(Math.class.getDeclaredMethod("max", Double.TYPE, Double.TYPE));
	}

	public static Stream<Arguments> expectedLinks() {
		return linksPerVersion( //
				since("1.1", "java.lang.Math.html#max(double, double)"), //
				since("1.2", "java/lang/Math.html#max(double, double)"), //
				since("8", "java/lang/Math.html#max-double-double-"), //
				since("10", "java/lang/Math.html#max(double,double)"), //
				since("11", "java.base/java/lang/Math.html#max(double,double)"));
	}

}
