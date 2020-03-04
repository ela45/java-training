package com.edu.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.edu.annotations.Controller;
import com.edu.annotations.Parameters;
import com.edu.annotations.RequestMapping;
import com.edu.exceptions.BadRequestException;
import com.edu.proxy.DynamicProxy;
import com.edu.utils.Constants;
import com.edu.utils.HttpStatus;
import com.edu.utils.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(DispatcherServlet.class);

	private static List<Class<?>> classes = new ArrayList<Class<?>>();
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	
	public void init() {
		 jdbcURL = getServletContext().getInitParameter("jdbcURL");
		 jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		 jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		getClassList();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String body = null;
		try {
			String requestURL = getURL(request);
			String httpMethod = request.getMethod();
			LOGGER.info("URL Request " + requestURL);
			LOGGER.info("Http Method " + httpMethod);
		
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			/*
			 * Get parameters request
			 */
			String params = request.getParameterMap().entrySet().stream()
					.map(entry -> entry.getKey() + ":" + Arrays.toString(entry.getValue()))
					.collect(Collectors.joining(", "));
			Map<String, String> requestDataValues = null;

			if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
				body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				requestDataValues = convertJSONToMap(body);
				LOGGER.info("Request body " + body);
			} else if (Objects.nonNull(params)) {
				requestDataValues = request.getParameterMap().entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey,
								entry -> Arrays.toString(entry.getValue()).replace("[", "").replace("]", "")));
				LOGGER.info("Request params " + params);

			}
			// ** Get controllers list **// 
			List<Class<?>> controllersList = classes.stream()
					.filter(clazz -> Objects.nonNull(clazz.getAnnotation(Controller.class)))
					.collect(Collectors.toList());
			controllersList.stream().forEach(clazz -> {
				LOGGER.info("Controller founded " + clazz.getName());
			});

			String[] dividedURL = requestURL.split("/");

			/*
			 * If exists a request mapping get the specific controller
			 */
			if (Objects.nonNull(dividedURL)) {
				Class<?> controllerClass = getRequestMappingController(dividedURL, controllersList);
				String responseController=getControllerMethodResponse(controllerClass, request, requestDataValues);
				if(Objects.isNull(responseController)) {
					response.setStatus(HttpStatus.NOT_FOUND_CODE);
					
				}else {
					response.setStatus(HttpStatus.OK);
					writeResponse(out,responseController);
				}
				
			} else {
				LOGGER.error("Malformed URL");
			}

		} catch (BadRequestException e) {
			response.setStatus(HttpStatus.NOT_FOUND_CODE);
			writeResponse(out,e.getMessage());
			LOGGER.error("The request can not be handdle bad request exception " + e.getMessage());
		}
		catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_CODE);
			writeResponse(out,e.getMessage());
			LOGGER.error("The request can not be handle ERROR 404. " + e.getMessage());
		}

	}

	private void writeResponse(PrintWriter printer,String data) {
		printer.print(data);
		printer.flush();
	}
	private Class<?> getRequestMappingController(String[] urlMapping, List<Class<?>> controllersList) {
		String urlController = urlMapping[1];
		Class<?> controllerClass = controllersList.stream()
				.filter(controller -> urlController.equals(controller.getAnnotation(RequestMapping.class).value()
						.substring(1, controller.getAnnotation(RequestMapping.class).value().length())))
				.findAny().orElse(null);

		if (Objects.nonNull(controllerClass)) {
			LOGGER.info("Controller found " + controllerClass.getName());
		}
		return controllerClass;
	}

	private Object[] mapMethodParameters(Map<String, String> valuesDataRequestMap,Parameter[] methodParameters) {
		Object [] parameters=new Object[methodParameters.length];
		
		AtomicInteger i=new AtomicInteger(0);
		Arrays.asList(methodParameters).stream().forEach(parameter->{
			String methorParameterName=parameter.getAnnotation(Parameters.class).name();
			parameters[i.get()]=valuesDataRequestMap.get(methorParameterName);
			i.getAndIncrement();
		});
		
		
		return parameters;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> convertJSONToMap(String json) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> bodyRequestMap = null;
		try {
			bodyRequestMap = mapper.readValue(json, Map.class);
			LOGGER.info("JSON to Convert " + json);
		} catch (IOException e) {
			LOGGER.error("The body request JSON can not be mapped");
		}
		return bodyRequestMap.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue())));
	}

	private String getControllerMethodResponse(Class<?> controllerClass, HttpServletRequest request,
			Map<String, String> valuesDataRequestMap) throws BadRequestException, Exception {
		Method[] methods = controllerClass.getDeclaredMethods();
		Method methodToCall = null;
		/*
		 * Find the annotations methods to map the corresponding httpMethod
		 */
		String[] dividedURL = getURL(request).split("/");
		List<Method> methodsList = Arrays.asList(methods).stream().filter(method -> Objects
				.nonNull(method.getAnnotation(RequestMapping.class))
				&& method.getAnnotation(RequestMapping.class).method()[0] == RequestMethod.valueOf(request.getMethod()))
				.collect(Collectors.toList());

		if (Objects.isNull(methodsList) || methodsList.isEmpty()) {
			throw new BadRequestException("No method to handle the request found ");
		}
		/*
		 * if there is more than one method to request httpMethod */
		if (methodsList.size() == 1) {
			methodToCall = methodsList.get(0);
		} else if (Objects.nonNull(dividedURL) && dividedURL.length > 2 && Objects.nonNull(dividedURL[2])) {
			String urlValueMapping = dividedURL[2];
	
			methodToCall=methodsList.stream()
						.filter(method->Objects.nonNull(method.getAnnotation(RequestMapping.class).value()) 
								&& method.getAnnotation(RequestMapping.class).value().replace("/", "").equals(urlValueMapping))
						.findFirst().orElse(null);
	
		}else if(dividedURL.length<=2) {
			
			methodToCall=methodsList.stream()
					.filter(method->Objects.isNull(method.getAnnotation(RequestMapping.class)) 
					|| method.getAnnotation(RequestMapping.class).value().replace("/", "").isEmpty())
					.findFirst().orElse(null);
		}
		
		else {
			LOGGER.error("Error there are more than one mapping request method");

			throw new BadRequestException("Error there are more than one mapping request method");
		}


		/*
		 * Get the corresponding interface method to call the proxy.
		 */
		Method methodInterface = null;
		for (Class<?> inter : controllerClass.getInterfaces()) {
			Method methodClass = inter.getMethod(methodToCall.getName(), methodToCall.getParameterTypes());
			if (Objects.nonNull(methodClass)) {
				methodInterface = methodClass;
			}
		}
		/*
		 * Convert the Json string body to Map to get parameters methods values
		 */
		Object[] parametersMethodValues = mapMethodParameters(valuesDataRequestMap,methodToCall.getParameters());

		/*
		 * Call the proxy
		 */
		Constructor<?> cons = controllerClass.getConstructor(String.class,String.class,String.class);
		Object object = cons.newInstance(jdbcURL,jdbcUsername,jdbcPassword);
		Object proxy = DynamicProxy.newInstance(object);
		Object controllerResponse=methodInterface.invoke(proxy, parametersMethodValues);
		
		String response=null;
		if(controllerResponse instanceof String) {
			response=(String)controllerResponse;
		}else {
			throw new Exception("Error on getting the response");
		}
		return response;
	}

	private void getClassList() {

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL[] urls = ((URLClassLoader) classLoader).getURLs();
		List<URL> urlsList = Arrays.asList(urls);
		URL urlClass = urlsList.stream().filter(url -> url.getFile().contains(Constants.CLASSES_FOLDER)).findAny()
				.orElse(null);
		if (Objects.nonNull(urlClass)) {
			File file = new File(urlClass.getFile());
			if (file.exists()) {
				findClasses(file, file, classLoader);
			} else {
				LOGGER.error("The file does not exists " + urlClass.getFile());
			}
		} else {
			LOGGER.error("Class folder not found");
		}

	}

	private static String getURL(HttpServletRequest request) {
		String uri = (String) request.getAttribute(Constants.INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return uri;
	}
	/*
	 * Finds all classes from application source*/
	private boolean findClasses(File root, File file, ClassLoader cl) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				if (!findClasses(root, child, cl)) {
					return false;
				}
			}
		} else {
			if (file.getName().toLowerCase().endsWith(Constants.CLASS_EXTENSION)) {
				String className = createClassName(root, file);
				try {
					classes.add(Class.forName(className));
				} catch (ClassNotFoundException e) {
					LOGGER.error("The class was not found " + className);
				}

			}
		}

		return true;
	}
	/*
	 * Returns a string with the class name in the
	 * format packageName.ClassName*/
	private String createClassName(File root, File file) {
		StringBuffer sb = new StringBuffer();
		String fileName = file.getName();
		sb.append(fileName.substring(0, fileName.lastIndexOf(Constants.CLASS_EXTENSION)));
		file = file.getParentFile();
		while (file != null && !file.equals(root)) {
			sb.insert(0, '.').insert(0, file.getName());
			file = file.getParentFile();
		}
		return sb.toString();
	}

}
