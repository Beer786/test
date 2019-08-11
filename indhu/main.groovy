import groovy.json.JsonSlurper
import org.apache.commons.codec.binary.Base64;

			String webPage = "http://192.168.43.238:7990/rest/api/1.0/projects/HALO/repos";
			String name = "shever";
			String password = "redhat";

			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
//            println (urlConnection.getInputStream())

def output = urlConnection.getInputStream().getText();

def data = new JsonSlurper().parseText(output)
def repo_list = data.values
def repo_names = [ ]
for ( repo in repo_list ) {
println ("utfvrefyjr")
println (repo.name)
repo_names.add(repo.name)
}
println (repo_names)

def repo_names = "sheverouhofuwqe"

def CreatePipelineJob() {
    pipelineJob("shever-testing") {
        logRotator {
            numToKeep(20)
        }
    environmentVariables {
            envs('repo_list': "${repo_names}")
        }

        //displayName("#${BUILD_NUMBER} ${ENV}")
        definition {
            cps {
                script(readFileFromWorkspace('indhu/pipeline.groovy'))
                sandbox()
                }
                }
                }

}
CreatePipelineJob()
