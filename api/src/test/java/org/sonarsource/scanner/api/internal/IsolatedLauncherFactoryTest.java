/*
 * SonarQube Scanner API
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.scanner.api.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.sonarsource.scanner.api.internal.ClassloadRules;
import org.sonarsource.scanner.api.internal.IsolatedLauncherFactory;
import org.sonarsource.scanner.api.internal.JarDownloader;
import org.sonarsource.scanner.api.internal.ScannerException;
import org.sonarsource.scanner.api.internal.TempCleaning;
import org.sonarsource.scanner.api.internal.batch.IsolatedLauncher;
import org.sonarsource.scanner.api.internal.batch.LogOutput;
import org.sonarsource.scanner.api.internal.cache.Logger;

import static org.fest.assertions.Fail.fail;
import static org.mockito.Mockito.mock;

public class IsolatedLauncherFactoryTest {
  IsolatedLauncherFactory factory;
  Properties props;
  TempCleaning tempCleaning;
  JarDownloader jarDownloader;

  @Before
  public void setUp() {
    tempCleaning = mock(TempCleaning.class);
    factory = new IsolatedLauncherFactory(FakeIsolatedLauncher.class.getName(), tempCleaning, mock(Logger.class));
    props = new Properties();
    jarDownloader = mock(JarDownloader.class);
  }

  @Test
  public void should_use_isolated_classloader() {
    try {
      factory.createLauncher(jarDownloader, new ClassloadRules(new HashSet<String>(), new HashSet<String>()));
      fail();
    } catch (ScannerException e) {
      // success
    }
  }

  public static class FakeIsolatedLauncher implements IsolatedLauncher {
    public static Properties props = null;

    @Override
    public void start(Properties properties, LogOutput logger) {
    }

    @Override
    public void stop() {
    }

    @Override
    public void execute(Properties properties) {
      FakeIsolatedLauncher.props = properties;
    }

    @Override
    public void executeOldVersion(Properties properties, List<Object> extensions) {
    }

    @Override
    public String getVersion() {
      return null;
    }

  }
}