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
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public abstract class JavaDocLinkTest {

	@ParameterizedTest
	@MethodSource("expectedLinks")
	public void should_create_correct_link_from_internal_names(String version, String expectedLink) throws Exception {
		assertEquals(expectedLink, createLink(JavaDocLink.forVersion(version)));
	}

	protected abstract String createLink(JavaDocLink jdl) throws Exception;

	@ParameterizedTest
	@MethodSource("expectedLinks")
	public void should_create_correct_link_from_refection(String version, String expectedLink) throws Exception {
		assertEquals(expectedLink, createLinkReflective(JavaDocLink.forVersion(version)));
	}

	protected abstract String createLinkReflective(JavaDocLink jdl) throws Exception;

	protected static Stream<Arguments> linksPerVersion(LinkSince... sincelist) {
		Map<String, String> links = new LinkedHashMap<>();
		for (var sincelink : sincelist) {
			String link = null;
			for (var version : JavaDocLink.supportedVersions()) {
				if (version.equals(sincelink.version)) {
					link = sincelink.link;
				}
				if (link != null) {
					links.put(version, link);
				}
			}
		}
		return links.entrySet().stream().map(e -> Arguments.of(e.getKey(), e.getValue()));
	}

	protected static class LinkSince {
		private String version;
		private String link;

		public LinkSince(String version, String link) {
			this.version = version;
			this.link = link;
		}
	}

	protected static LinkSince since(String version, String link) {
		return new LinkSince(version, link);
	}

	@Tag("webaccess")
	@ParameterizedTest
	@MethodSource("expectedLinks")
	public void created_link_should_exist_in_actual_javadoc(String version, String expectedLink) throws Exception {
		JavaDocLink jdl = JavaDocLink.forVersion(version).withBaseUrl(JDK_API_DOC.get(version));
		var urlstr = createLink(jdl);
		Document doc = Jsoup.connect(urlstr).get();
		var ref = new URL(urlstr).getRef();
		if (ref != null) {
			ref = URLDecoder.decode(ref, StandardCharsets.US_ASCII);
			if (hasNoElement(doc, "a", "id", ref) && //
					hasNoElement(doc, "a", "name", ref) && //
					hasNoElement(doc, "section", "id", ref)) {
				fail("Anchor not found: " + ref);
			}
		}
	}

	private boolean hasNoElement(Document doc, String element, String attr, String value) {
		return doc.select(String.format("%s[%s=%s]", element, attr, value)).isEmpty();
	}

	private static final Map<String, String> JDK_API_DOC = new HashMap<>();

	static {
		JDK_API_DOC.put("1.1", "https://javaalmanac.io/jdk/1.1/api/");
		JDK_API_DOC.put("1.2", "https://javaalmanac.io/jdk/1.2/api/");
		JDK_API_DOC.put("1.3", "https://javaalmanac.io/jdk/1.3/api/");
		JDK_API_DOC.put("1.4", "https://javaalmanac.io/jdk/1.4/api/");
		JDK_API_DOC.put("5", "https://docs.oracle.com/javase/1.5.0/docs/api/");
		JDK_API_DOC.put("6", "https://docs.oracle.com/javase/6/docs/api/");
		JDK_API_DOC.put("7", "https://docs.oracle.com/javase/7/docs/api/");
		JDK_API_DOC.put("8", "https://docs.oracle.com/javase/8/docs/api/");
		JDK_API_DOC.put("9", "https://docs.oracle.com/javase/9/docs/api/");
		JDK_API_DOC.put("10", "https://docs.oracle.com/javase/10/docs/api/");
		JDK_API_DOC.put("11", "https://docs.oracle.com/en/java/javase/11/docs/api/");
		JDK_API_DOC.put("12", "https://docs.oracle.com/en/java/javase/12/docs/api/");
		JDK_API_DOC.put("13", "https://docs.oracle.com/en/java/javase/13/docs/api/");
		JDK_API_DOC.put("14", "https://docs.oracle.com/en/java/javase/14/docs/api/");
		JDK_API_DOC.put("15", "https://docs.oracle.com/en/java/javase/15/docs/api/");
		JDK_API_DOC.put("16", "https://docs.oracle.com/en/java/javase/16/docs/api/");
		JDK_API_DOC.put("17", "https://docs.oracle.com/en/java/javase/17/docs/api/");
		JDK_API_DOC.put("18", "https://download.java.net/java/early_access/jdk18/docs/api/");
		JDK_API_DOC.put("19", "https://download.java.net/java/early_access/jdk19/docs/api/");
	}

}
