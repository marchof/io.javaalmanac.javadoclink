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

public class PackageTest extends JavaDocLinkTest {

	@Override
	protected String createLink(JavaDocLink jdl) {
		return jdl.packageLink("java.base", "java/lang");
	}

	@Override
	protected String createLinkReflective(JavaDocLink jdl) {
		var cls = String.class;
		return jdl.packageLink(cls.getModule(), cls.getPackage());
	}

	public static Stream<Arguments> expectedLinks() {
		return linksPerVersion( //
				since("1.1", "Package-java.lang.html"), //
				since("1.2", "java/lang/package-summary.html"), //
				since("11", "java.base/java/lang/package-summary.html"));
	}

}
