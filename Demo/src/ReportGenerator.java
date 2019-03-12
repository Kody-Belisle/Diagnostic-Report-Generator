package src;

/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2009 Pentaho Corporation.  All rights reserved.
 *
 * Created July 22, 2009
 * @author dkincade
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;
import java.util.HashMap;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ParameterDataRow;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.DriverConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.SQLReportDataFactory;
import org.pentaho.reporting.engine.classic.core.wizard.RelationalAutoGeneratorPreProcessor;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import javax.swing.table.TableModel;

/**
 * Generates a report in the following scenario:
 * <ol>
 * <li>The report definition file is a .prpt file which will be loaded and parsed
 * <li>The data factory is a simple JDBC data factory using HSQLDB
 * <li>There are no runtime report parameters used
 * </ol>
 */
public class ReportGenerator extends AbstractReportGenerator
{

    private Report report;
    private DerbyDao dao;
    /**
     * Default constructor for this sample report generator
     */
    public ReportGenerator(Report report)
    {
        this.report = report;
        this.dao = new DerbyDao();

        //Super basic testing for the DAO
        //Double od = 0.281;
        //dao.addReport("O456", "BLV", "Black Pond Acres", "2019-02-24", "2019-02-27", "results.txt");
        //dao.addAnimal("Bess", "Sheep", od, "Positive", "Black Pond Acres", "O456");
        //dao.addAnimal("Bess", "Sheep", od, "Negative", "Old McDonald Farms", "O123");
    }

    private static final String QUERY_NAME = "ReportQuery";
    /**
     * Returns the report definition which will be used to generate the report. In this case, the report will be
     * loaded and parsed from a file contained in this package.
     *
     * @return the loaded and parsed report definition to be used in report generation.
     */
    public MasterReport getReportDefinition()
    {
        try
        {
            // Using the classloader, get the URL to the reportDefinition file
            final ClassLoader classloader = this.getClass().getClassLoader();
            final URL reportDefinitionURL = this.getClass().getResource("../SageReportTemplate.prpt");
            //final URL reportDefinitionURL = classloader.getResource("/src/sample1.prpt");
            System.out.println(reportDefinitionURL);
            //System.out.println(testUrl);
            // Parse the report file
            final ResourceManager resourceManager = new ResourceManager();
            resourceManager.registerDefaults();
            final Resource directly = resourceManager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) directly.getResource();
            report.setQuery(QUERY_NAME);
            report.addPreProcessor(new RelationalAutoGeneratorPreProcessor());
            //return (MasterReport) directly.getResource();
            return report;
        }
        catch (ResourceException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the data factory which will be used to generate the data used during report generation. In this example,
     * we will return null since the data factory has been defined in the report definition.
     *
     * @return the data factory used with the report generator
     */
    public DataFactory getDataFactory()
    {
        final DriverConnectionProvider connProvider = dao.setUpConnProvider();

        final SQLReportDataFactory dataFactory = new SQLReportDataFactory(connProvider);
        dataFactory.setQuery(QUERY_NAME,
                "select client_name, name from Client");

        return dataFactory;
        //return null;
    }

    /**
     * Returns the set of runtime report parameters. This sample report uses the following three parameters:
     * <ul>
     * <li><b>Report Title</b> - The title text on the top of the report</li>
     * <li><b>Customer Names</b> - an array of customer names to show in the report</li>
     * <li><b>Col Headers BG Color</b> - the background color for the column headers</li>
     * </ul>
     *
     * @return <code>null</code> indicating the report generator does not use any report parameters
     */
    public Map<String, Object> getReportParameters()
    {
        final Map parameters = new HashMap<String, Object>();

        if (report == null) {
            System.out.println("ERROR: no report passed in");
            return null;
        }
        System.out.println("Creating report for" + report.getSingleClient().getCompanyName());

        parameters.put("TEST_TYPE", report.getTestType() + " Report");

        parameters.put("OWNER_ADDRESS", "1187 Edgemont Ln");
        parameters.put("OWNER_CITY_STATE_ZIP", ("Emmett, ID 83716"));
        parameters.put("OWNER_PHONE", "(208) 963-5679");
        parameters.put("OWNER_EMAIL", "lab@sageaglab.com");
        parameters.put("OWNER_WEBSITE", "www.sageaglab.com");

        //client info
        parameters.put("COMPANY_NAME", report.getSingleClient().getCompanyName());

        parameters.put("CLIENT_ADDRESS", report.getSingleClient().getAddress());
        parameters.put("CLIENT_CITY_STATE_ZIP", report.getSingleClient().getCity() + " " + report.getSingleClient().getState() +", " + report.getSingleClient().getZip());

        parameters.put("LOGID", "LOG O10419004");

        parameters.put("DATE_RECEIVED", "1/4/2019");
        parameters.put("DATE_TESTED", "1/9/2019");


        //test data
        parameters.put("POSITIVE_OD", "0.698");
        parameters.put("MARGINAL_LOW", "0.398");
        parameters.put("MARGINAL_HIGH", "0.698");
        return parameters;
        //return null;
    }

    /**
     * Simple command line application that will generate a PDF version of the report. In this report,
     * the report definition has already been created with the Pentaho Report Designer application and
     * it located in the same package as this class. The data query is located in that report definition
     * as well, and there are a few report-modifying parameters that will be passed to the engine at runtime.
     * <p/>
     * The output of this report will be a PDF file located in the current directory and will be named
     * <code>SimpleReportGeneratorExample.pdf</code>.
     *
     * @param args none
     * @throws IOException indicates an error writing to the filesystem
     * @throws ReportProcessingException indicates an error generating the report
     */
    public static void main(String[] args) throws IOException, ReportProcessingException
    {
        // Create an output filename
        final File outputFilename = new File(ReportGenerator.class.getSimpleName() + ".pdf");

        // Generate the report
        new ReportGenerator(null).generateReport(AbstractReportGenerator.OutputType.PDF, outputFilename);

        // Output the location of the file
        System.err.println("Generated the report [" + outputFilename.getAbsolutePath() + "]");

    }
}