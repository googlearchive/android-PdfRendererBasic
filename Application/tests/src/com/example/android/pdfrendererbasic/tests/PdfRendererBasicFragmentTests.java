/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.pdfrendererbasic.tests;

import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.Button;

import com.example.android.pdfrendererbasic.MainActivity;
import com.example.android.pdfrendererbasic.PdfRendererBasicFragment;
import com.example.android.pdfrendererbasic.R;

/**
 * Tests for PdfRendererBasic sample.
 */
public class PdfRendererBasicFragmentTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private PdfRendererBasicFragment mFragment;

    private Button mButtonPrevious;
    private Button mButtonNext;

    public PdfRendererBasicFragmentTests() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mFragment = (PdfRendererBasicFragment) mActivity.getFragmentManager()
                .findFragmentByTag(MainActivity.FRAGMENT_PDF_RENDERER_BASIC);
    }

    @LargeTest
    public void testActivityTitle() {
        // The title of the activity should be "PdfRendererBasic (1/10)" at first
        String expectedActivityTitle = mActivity.getString(R.string.app_name_with_index, 1,
                mFragment.getPageCount());
        assertEquals(expectedActivityTitle, mActivity.getTitle());
    }

    @LargeTest
    public void testButtons_previousDisabledAtFirst() {
        setUpButtons();
        // Check that the previous button is disabled at first
        assertFalse(mButtonPrevious.isEnabled());
        // The next button should be enabled
        assertTrue(mButtonNext.isEnabled());
    }

    @LargeTest
    public void testButtons_bothEnabledInMiddle() {
        setUpButtons();
        turnPages(1);
        // Two buttons should be both enabled
        assertTrue(mButtonPrevious.isEnabled());
        assertTrue(mButtonNext.isEnabled());
    }

    @LargeTest
    public void testButtons_nextDisabledLastPage() {
        setUpButtons();
        int pageCount = mFragment.getPageCount();
        // Click till it reaches the last page
        turnPages(pageCount - 1);
        // Check the page count
        String expectedActivityTitle = mActivity.getString(R.string.app_name_with_index,
                pageCount, pageCount);
        assertEquals(expectedActivityTitle, mActivity.getTitle());
        // The previous button should be enabled
        assertTrue(mButtonPrevious.isEnabled());
        // Check that the next button is disabled
        assertFalse(mButtonNext.isEnabled());
    }

    @LargeTest
    public void testOrientationChangePreserveState() {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setUpButtons();
        turnPages(1);
        int pageCount = mFragment.getPageCount();
        String expectedActivityTitle = mActivity.getString(R.string.app_name_with_index,
                2, pageCount);
        assertEquals(expectedActivityTitle, mActivity.getTitle());
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Check that the title is the same after orientation change
        assertEquals(expectedActivityTitle, mActivity.getTitle());
    }

    /**
     * Prepares references to the buttons "Previous" and "Next".
     */
    private void setUpButtons() {
        View view = mFragment.getView();
        assertNotNull(view);
        mButtonPrevious = (Button) view.findViewById(R.id.previous);
        assertNotNull(mButtonPrevious);
        mButtonNext = (Button) view.findViewById(R.id.next);
        assertNotNull(mButtonNext);
    }

    /**
     * Click the "Next" button to turn the pages.
     *
     * @param count The number of times to turn pages.
     */
    private void turnPages(int count) {
        for (int i = 0; i < count; ++i) {
            TouchUtils.clickView(this, mButtonNext);
        }
    }

}
