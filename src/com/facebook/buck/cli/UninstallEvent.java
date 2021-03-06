/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.cli;

import com.facebook.buck.event.AbstractBuckEvent;
import com.facebook.buck.event.BuckEvent;
import com.facebook.buck.event.LeafEvent;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

@SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
public abstract class UninstallEvent extends AbstractBuckEvent implements LeafEvent {
  private final String packageName;

  public UninstallEvent(String packageName) {
    this.packageName = Preconditions.checkNotNull(packageName);
  }

  public String getPackageName() {
    return packageName;
  }

  @Override
  public String getCategory() {
    return "uninstall_apk";
  }

  @Override
  protected String getValueString() {
    return packageName;
  }

  @Override
  public boolean eventsArePair(BuckEvent event) {
    if (!(event instanceof UninstallEvent)) {
      return false;
    }

    UninstallEvent that = (UninstallEvent) event;

    return Objects.equal(getPackageName(), that.getPackageName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPackageName());
  }

  public static Started started(String packageName) {
    return new Started(packageName);
  }

  public static Finished finished(String packageName, boolean success) {
    return new Finished(packageName, success);
  }

  public static class Started extends UninstallEvent {
    protected Started(String packageName) {
      super(packageName);
    }

    @Override
    public String getEventName() {
      return "UninstallStarted";
    }
  }

  public static class Finished extends UninstallEvent {
    private final boolean success;

    protected Finished(String packageName, boolean success) {
      super(packageName);

      this.success = success;
    }

    public boolean isSuccess() {
      return success;
    }

    @Override
    public String getEventName() {
      return "UninstallFinished";
    }

    @Override
    public boolean equals(Object o) {
      if (!super.equals(o)) {
        return false;
      }

      Finished that = (Finished) o;
      return isSuccess() == that.isSuccess();
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(getPackageName(), isSuccess());
    }
  }
}
