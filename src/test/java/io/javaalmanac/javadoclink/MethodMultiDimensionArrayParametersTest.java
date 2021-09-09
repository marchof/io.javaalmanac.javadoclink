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

import java.text.DateFormatSymbols;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class MethodMultiDimensionArrayParametersTest extends JavaDocLinkTest {

	@Override
	protected String createLink(JavaDocLink jdl) {
		return jdl.methodLink("java.base", "java/text/DateFormatSymbols", "setZoneStrings", "([[Ljava/lang/String;)V",
				false);
	}

	@Override
	protected String createLinkReflective(JavaDocLink jdl) throws Exception {
		return jdl.methodLink(DateFormatSymbols.class.getDeclaredMethod("setZoneStrings", String[][].class));
	}

	public static Stream<Arguments> expectedLinks() {
		return linksPerVersion( //
				since("1.1", "java.text.DateFormatSymbols.html#setZoneStrings(java.lang.String%5B%5D%5B%5D)"), //
				since("1.2", "java/text/DateFormatSymbols.html#setZoneStrings(java.lang.String%5B%5D%5B%5D)"), //
				since("8", "java/text/DateFormatSymbols.html#setZoneStrings-java.lang.String:A:A-"), //
				since("10", "java/text/DateFormatSymbols.html#setZoneStrings(java.lang.String%5B%5D%5B%5D)"), //
				since("11", "java.base/java/text/DateFormatSymbols.html#setZoneStrings(java.lang.String%5B%5D%5B%5D)"));
	}

}
