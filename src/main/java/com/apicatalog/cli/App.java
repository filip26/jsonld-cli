package com.apicatalog.cli;

import com.apicatalog.cli.command.CompactCmd;
import com.apicatalog.cli.command.CompressCmd;
import com.apicatalog.cli.command.ExpandCmd;
import com.apicatalog.cli.command.FlattenCmd;
import com.apicatalog.cli.command.FrameCmd;
import com.apicatalog.cli.command.FromRdfCmd;
import com.apicatalog.cli.command.ToRdfCmd;
import com.apicatalog.jsonld.http.media.MediaType;
import com.apicatalog.jsonld.loader.HttpLoader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;

@Command(
    name = "ld-cli",
    description = "Linked Data Command Line Processor",
    subcommands = { 
            ExpandCmd.class,
            CompactCmd.class,
            FlattenCmd.class,
            FrameCmd.class,
            FromRdfCmd.class,
            ToRdfCmd.class,
            CompressCmd.class,
            },
    mixinStandardHelpOptions = false,
    descriptionHeading = "%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    commandListHeading = "%nCommands:%n",
    version = {
            "json-ld-cli       0.8.0  https://github.com/filip26/json-ld-cli",
            "titanium-json-ld  1.3.1  https://github.com/filip26/titanium-json-ld",
            "iridium-cbor-ld   0.1.0  https://github.com/filip26/iridium-cbor-ld",
            }
    )
public final class App {

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "display help message")
    boolean help = false;

    @Option(names = {"-v", "--version"}, versionHelp = true, description = "display a version")
    boolean version;    

    static {
        ((HttpLoader) HttpLoader.defaultInstance()).setFallbackContentType(MediaType.JSON);
    }
    
    public static void main(String[] args) {

        final CommandLine cli = new CommandLine(new App());
        cli.setCaseInsensitiveEnumValuesAllowed(true);

        try {

            final ParseResult result = cli.parseArgs(args);

            if (cli.isUsageHelpRequested()) {

                if (result.subcommand() != null) {
                    result.subcommand().commandSpec().commandLine().usage(cli.getOut());
                    return;
                }

                cli.usage(cli.getOut());
                System.exit(cli.getCommandSpec().exitCodeOnUsageHelp());
                return;
            }

            if (cli.isVersionHelpRequested()) {
                cli.printVersionHelp(cli.getOut());
                System.exit(cli.getCommandSpec().exitCodeOnVersionHelp());
                return;
            }

            System.exit(cli.execute(args));

        } catch (Exception ex) {
            cli.getErr().println(ex.getMessage());
            System.exit(cli.getCommandSpec().exitCodeOnExecutionException());
        }
    }
}
