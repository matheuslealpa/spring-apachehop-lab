package com.exemplo.springapachehoplab;

import lombok.extern.slf4j.Slf4j;
import org.apache.hop.core.HopEnvironment;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.core.variables.Variables;
import org.apache.hop.core.xml.XmlParserFactoryProducer;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.config.PipelineRunConfiguration;
import org.apache.hop.pipeline.engine.IPipelineEngine;
import org.apache.hop.pipeline.engine.PipelineEngineFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@SpringBootApplication
public class SpringApachehopLabApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringApachehopLabApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            IVariables variables = new Variables();
            variables.initializeFrom(null);

            File hplFile = new ClassPathResource("pipelines/pipelines-hop.hpl").getFile();
            String caminhoPipeline = hplFile.getAbsolutePath();
            String xml = Files.readString(Path.of(caminhoPipeline));

            Document document = XmlParserFactoryProducer.createSecureDocBuilderFactory()
                    .newDocumentBuilder().parse(new InputSource(new StringReader(xml)));

            PipelineMeta pipelineMeta = new PipelineMeta();
            pipelineMeta.loadXml(String.valueOf(document.getDocumentElement()), null, null);
            pipelineMeta.setName("pipelines-hop");
            pipelineMeta.setFilename(caminhoPipeline);

            var metadataProvider = pipelineMeta.getMetadataProvider();
            if (metadataProvider == null) {
                log.warn("MetadataProvider estava nulo. Criando fallback.");
                metadataProvider = new org.apache.hop.metadata.serializer.memory.MemoryMetadataProvider();
            }

            IPipelineEngine<PipelineMeta> pipeline = PipelineEngineFactory.createPipelineEngine(
                    variables,
                    "Local",
                    metadataProvider,
                    pipelineMeta
            );

            pipeline.prepareExecution();
            pipeline.startThreads();
            pipeline.waitUntilFinished();

            if (pipeline.getErrors() > 0) {
                log.error("Pipeline executada com erros: {}", pipeline.getErrors());
            } else {
                log.info("Pipeline executada com sucesso!");
            }

        } catch (Exception e) {
            log.error("Erro ao executar pipeline Hop", e);
            throw e;
        }
    }

}
