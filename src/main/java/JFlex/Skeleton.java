/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4.3                                                             *
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
package JFlex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Vector;


/**
 * This class stores the skeleton of generated scanners.
 * <p/>
 * The skeleton consists of several parts that can be emitted to
 * a file. Usually there is a portion of generated code
 * (produced in class Emitter) between every two parts of skeleton code.
 * <p/>
 * There is a static part (the skeleton code) and state based iterator
 * part to this class. The iterator part is used to emit consecutive skeleton
 * sections to some <code>PrintWriter</code>.
 *
 * @author Gerwin Klein
 * @version $Revision: 1.4.3 $, $Date: 2009/12/21 15:58:48 $
 * @see JFlex.Emitter
 */
public class Skeleton
{

	/**
	 * location of default skeleton
	 */
	static final private String DEFAULT_LOC = "JFlex/skeleton.default"; //$NON-NLS-1$

	/**
	 * expected number of sections in the skeleton file
	 */
	static final private int size = 21;

	/**
	 * platform specific newline
	 */
	static final private String NL = System.getProperty("line.separator");  //$NON-NLS-1$

	/**
	 * The skeleton
	 */
	public static String line[];

	/** initialization */
	static
	{
		readDefault();
	}

	// the state based, iterator part of Skeleton:

	/**
	 * The current part of the skeleton (an index of nextStop[])
	 */
	private int pos;

	/**
	 * The writer to write the skeleton-parts to
	 */
	private PrintWriter out;


	/**
	 * Creates a new skeleton (iterator) instance.
	 *
	 * @param out the writer to write the skeleton-parts to
	 */
	public Skeleton(PrintWriter out)
	{
		this.out = out;
	}


	/**
	 * Emits the next part of the skeleton
	 */
	public void emitNext()
	{
		out.print(line[pos++]);
	}


	/**
	 * Make the skeleton private.
	 * <p/>
	 * Replaces all occurences of " public " in the skeleton with " private ".
	 */
	public static void makePrivate()
	{
		for(int i = 0; i < line.length; i++)
		{
			line[i] = replace(" public ", " private ", line[i]);   //$NON-NLS-1$ //$NON-NLS-2$
		}
	}


	/**
	 * Reads an skeleton file from resources for later use with this class.
	 *
	 * @param skeletonResource the file to read (must be != null and readable)
	 */
	public static void readSkelResource(String skeletonResource)
	{
		if(skeletonResource == null)
		{
			throw new IllegalArgumentException("Skeleton file must not be null"); //$NON-NLS-1$
		}

		InputStream resourceAsStream = Skeleton.class.getResourceAsStream(skeletonResource);
		if(resourceAsStream == null)
		{
			Out.error(ErrorMessages.CANNOT_READ_SKEL, skeletonResource.toString());
			throw new GeneratorException();
		}

		Out.println(ErrorMessages.READING_SKEL, skeletonResource.toString());

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
			readSkel(reader);
		}
		catch(IOException e)
		{
			Out.error(ErrorMessages.SKEL_IO_ERROR);
			throw new GeneratorException();
		}
	}

	/**
	 * Reads an skeleton file from input steam for later use with this class.
	 *
	 * @param stream the file to read (must be != null and readable)
	 */
	public static void readSkelSteam(InputStream stream)
	{
		if(stream == null)
		{
			throw new IllegalArgumentException("Skeleton steam must not be null"); //$NON-NLS-1$
		}

		Out.println(ErrorMessages.READING_SKEL, stream.toString());

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			readSkel(reader);
		}
		catch(IOException e)
		{
			Out.error(ErrorMessages.SKEL_IO_ERROR);
			throw new GeneratorException();
		}
	}

	/**
	 * Reads an external skeleton file for later use with this class.
	 *
	 * @param skeletonFile the file to read (must be != null and readable)
	 */
	public static void readSkelFile(File skeletonFile)
	{
		if(skeletonFile == null)
		{
			throw new IllegalArgumentException("Skeleton file must not be null"); //$NON-NLS-1$
		}

		if(!skeletonFile.isFile() || !skeletonFile.canRead())
		{
			Out.error(ErrorMessages.CANNOT_READ_SKEL, skeletonFile.toString());
			throw new GeneratorException();
		}

		Out.println(ErrorMessages.READING_SKEL, skeletonFile.toString());

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(skeletonFile));
			readSkel(reader);
		}
		catch(IOException e)
		{
			Out.error(ErrorMessages.SKEL_IO_ERROR);
			throw new GeneratorException();
		}
	}


	/**
	 * Reads an external skeleton file from a BufferedReader.
	 *
	 * @param reader the reader to read from (must be != null)
	 * @throws IOException        if an IO error occurs
	 * @throws GeneratorException if the number of skeleton sections does not match
	 */
	public static void readSkel(BufferedReader reader) throws IOException
	{
		Vector lines = new Vector();
		StringBuffer section = new StringBuffer();

		String ln;
		while((ln = reader.readLine()) != null)
		{
			if(ln.startsWith("---"))
			{ //$NON-NLS-1$
				lines.addElement(section.toString());
				section.setLength(0);
			}
			else
			{
				section.append(ln);
				section.append(NL);
			}
		}

		if(section.length() > 0)
		{
			lines.addElement(section.toString());
		}

		if(lines.size() != size)
		{
			Out.error(ErrorMessages.WRONG_SKELETON);
			throw new GeneratorException();
		}

		line = new String[size];
		for(int i = 0; i < size; i++)
		{
			line[i] = (String) lines.elementAt(i);
		}
	}

	/**
	 * Replaces a with b in c.
	 *
	 * @param a the String to be replaced
	 * @param b the replacement
	 * @param c the String in which to replace a by b
	 * @return a String object with a replaced by b in c
	 */
	public static String replace(String a, String b, String c)
	{
		StringBuffer result = new StringBuffer(c.length());
		int i = 0;
		int j = c.indexOf(a);

		while(j >= i)
		{
			result.append(c.substring(i, j));
			result.append(b);
			i = j + a.length();
			j = c.indexOf(a, i);
		}

		result.append(c.substring(i, c.length()));

		return result.toString();
	}


	/**
	 * (Re)load the default skeleton. Looks in the current system class path.
	 */
	public static void readDefault()
	{
		ClassLoader l = Skeleton.class.getClassLoader();
		URL url;

    /* Try to load from same class loader as this class.
     * Should work, but does not on OS/2 JDK 1.1.8 (see bug 1065521).
     * Use system class loader in this case.
     */
		if(l != null)
		{
			url = l.getResource(DEFAULT_LOC);
		}
		else
		{
			url = ClassLoader.getSystemResource(DEFAULT_LOC);
		}

		if(url == null)
		{
			Out.error(ErrorMessages.SKEL_IO_ERROR_DEFAULT);
			throw new GeneratorException();
		}

		try
		{
			InputStreamReader reader = new InputStreamReader(url.openStream());
			readSkel(new BufferedReader(reader));
		}
		catch(IOException e)
		{
			Out.error(ErrorMessages.SKEL_IO_ERROR_DEFAULT);
			throw new GeneratorException();
		}
	}
}
