/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * jflex                                                         *
 * Copyright (C) 1998-2009  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package JFlex.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * AllTests
 * 
 * @author Gerwin Klein
 * @version $Revision: 1.4.3 $, $Date: 2009/12/21 15:58:48 $
 */
public class AllTests extends TestCase {
  public static Test suite() {
    TestSuite suite = new TestSuite("JFlex tests");
    //$JUnit-BEGIN$
    suite.addTest(new TestSuite(AntTaskTests.class));
    suite.addTest(new TestSuite(CharClassesTest.class));
    suite.addTest(new TestSuite(EmitterTest.class));
    suite.addTest(new TestSuite(RegExpTests.class));
    suite.addTest(new TestSuite(SkeletonTest.class));
    suite.addTest(new TestSuite(PackEmitterTest.class));
    //$JUnit-END$
    return suite;
  }
}
