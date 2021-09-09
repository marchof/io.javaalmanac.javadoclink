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

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class InnerClassTest extends JavaDocLinkTest {

	@Override
	protected String createLink(JavaDocLink jdl) {
		return jdl.classLink("java.base", "java/util/Map$Entry");
	}

	@Override
	protected String createLinkReflective(JavaDocLink jdl) {
		return jdl.classLink(Map.Entry.class);
	}

	public static Stream<Arguments> expectedLinks() {
		return linksPerVersion( //
				since("1.2", "java/util/Map.Entry.html"), //
				since("11", "java.base/java/util/Map.Entry.html"));
	}

}
