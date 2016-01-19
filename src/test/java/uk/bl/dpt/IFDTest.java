/**
 * Copyright 2016 Peter May
 * Author: Peter May
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.bl.dpt;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests relating to the IFD Class.
 *
 * Tests for:
 *  1) Empty IFD Creation
 *  2) Correct StripOffset for data in one IFD
 *  3) Correct StripOffset for data split across 2 IFDs
 *
 * Todo tests:
 *  4) numerically ordered Directory entries in IFD
 */
public class IFDTest {

    /**
     * 1: Tests that an empty IFD can be created with no tags.
     * This is invalid according to the TIFF v6 spec as an IFD MUST contain at least 1 entry [pg14].
     */
    @Test
    public void emptyIFDTest(){
        IFD ifd = new IFD();
        assertEquals(0, ifd.numberOfDirectories());
    }

    /**
     * 2: Tests that a correct StripOffsets is returned for a TIFF where the image
     * data is all in one strip.
     */
    @Test
    public void nonSplitStripOffsetTest(){
        IFD ifd = new IFD();
        ifd.addDirectory(IFDTag.StripOffsets, IFDType.LONG, 1, new Integer[]{8});
        assertEquals(1, ifd.numberOfDirectories());

        IFD.Directory<Integer> directory = ifd.getDirectory(IFDTag.StripOffsets);
        assertArrayEquals(new Integer[]{8}, directory.getValue());
    }

    /**
     * 3: Tests that the correct StripOffset locations are returned for a TIFF whose
     * image data is split into 2 strips.
     */
    @Test
    public void twoSplitStripOffsetTest(){
        IFD ifd = new IFD();
        ifd.addDirectory(IFDTag.StripOffsets, IFDType.LONG, 2, new Integer[]{8, 0x1e8});
        assertEquals(1, ifd.numberOfDirectories());

        IFD.Directory<Integer> directory = ifd.getDirectory(IFDTag.StripOffsets);
        assertEquals(2, directory.getCount());
        assertArrayEquals(new Integer[]{8, 0x1e8}, directory.getValue());
        assertEquals(new Integer(8), directory.getValue()[0]);
        assertEquals(new Integer(0x1e8), directory.getValue()[1]);
    }
}
