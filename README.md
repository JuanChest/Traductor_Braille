# TRADUCTOR BRAILLE

## Versión 1.3.0

### Escuela Politécnica Nacional

Proyecto realizado para la asignatura de Construcción y evolución del Software.
Periodo: 2025B

Desarrolladores:

- Anguaya Angel
- Arrobo Julio
- Cofre Juan
- Parra Jhordy

---

## Finalidad del Programa

**Traductor Braille** es una aplicación de escritorio desarrollada en Java con JavaFX que tiene como objetivo **democratizar el acceso a la creación de contenido en Braille**, eliminando las barreras técnicas y económicas que tradicionalmente han limitado la producción de materiales accesibles para personas con discapacidad visual.

### Contexto y Propósito

La mayoría de las personas sin discapacidad visual desconocen cómo generar contenido en Braille, lo que dificulta la creación de señalética, etiquetas, documentos educativos y materiales inclusivos en espacios físicos, productos y servicios. Las soluciones comerciales existentes suelen ser costosas, complejas o requieren equipos especializados.

Este sistema nace con el propósito de:

- **Facilitar la inclusión**: Permitir que cualquier persona pueda crear contenido en Braille de forma simple e intuitiva.
- **Reducir costos**: Ofrecer una herramienta gratuita que elimina la necesidad de contratar servicios especializados para traducciones básicas.
- **Promover la accesibilidad**: Incentivar la creación de espacios, productos y documentos más inclusivos mediante la democratización de la tecnología Braille.
- **Educar y sensibilizar**: Servir como herramienta educativa para que personas sin discapacidad visual comprendan el sistema Braille.

---

## Características Principales del Sistema

### 🔄 **1. Traducción Bidireccional Español ↔ Braille Unicode**

#### **Español a Braille**
- Convierte texto en español a su representación en Braille Unicode (caracteres U+2800 a U+28FF).
- Interfaz de texto tradicional con áreas de entrada y salida.
- Traducción instantánea mediante botón "Traducir".

#### **Braille a Español (v1.3.0)**
El sistema ahora ofrece **dos métodos** de ingreso de caracteres Braille:

**🖱️ Método 1: Entrada Interactiva con Celda Braille (Novedad v1.3.0)**
- **Celda Braille Visual**: Interfaz gráfica con 6 checkboxes que representan los 6 puntos del sistema Braille.
- **Ingreso Dual**:
  - **Con Mouse**: Haz clic directamente en los checkboxes para activar/desactivar puntos.
  - **Con Teclado**: Usa las teclas numéricas 1-6 (teclado principal o numérico) para activar/desactivar puntos.
- **Controles de Navegación**:
  - `ENTER`: Confirma el patrón de puntos actual y lo añade como símbolo Braille al área de texto.
  - `ESPACIO`: Inserta un espacio Braille (⠀) en el texto.
  - `BACKSPACE`: Borra el último símbolo ingresado.
- **Visualización en Tiempo Real**: Los checkboxes se actualizan visualmente al presionar teclas o hacer clic.
- **Acumulación de Símbolos**: Los símbolos confirmados se acumulan en un área de texto dedicada.
- **Traducción por Lotes**: Botón "Traducir a Español" procesa todos los símbolos acumulados y muestra el resultado.

**⌨️ Método 2: Entrada Directa de Unicode (Tradicional)**
- Pega o escribe directamente caracteres Braille Unicode en el área de texto.
- Compatible con copiar/pegar desde documentos o PDFs que contengan Braille Unicode.

**🔄 Funciones Adicionales**:
- **Botón "Limpiar"**: Reinicia tanto el área de símbolos Braille como el resultado en español.
- **Validación**: El sistema verifica que se hayan ingresado símbolos antes de traducir.

### 📝 **2. Soporte Completo del Alfabeto Español**

- **Alfabeto completo**: Todas las letras de la A-Z en minúsculas.
- **Letras con acentos**: á, é, í, ó, ú (con sus correspondientes patrones Braille).
- **Caracteres especiales del español**: ñ, ü.
- **Mayúsculas**: Manejo inteligente de letras mayúsculas mediante prefijos Braille:
  - Una sola letra en mayúscula: Se agrega un prefijo (⠨).
  - Palabras completas en mayúsculas: Se agregan dos prefijos consecutivos al inicio de la palabra para indicar que toda la palabra está en mayúsculas.

### 🔢 **3. Soporte de Números**

- **Dígitos del 0-9**: Traducción de todos los dígitos numéricos.
- **Prefijo automático**: El sistema detecta números automáticamente y agrega el prefijo numérico (⠼) antes de la secuencia de dígitos.
- **Modo número inteligente**: Se mantiene activo durante toda la secuencia numérica y se desactiva automáticamente al encontrar un espacio o letra.

### ✏️ **4. Signos de Puntuación y Caracteres Especiales**

El sistema soporta una amplia gama de caracteres:

- **Puntuación básica**: punto (.), coma (,), punto y coma (;), dos puntos (:)
- **Signos de interrogación y exclamación**: ¿, ?, ¡, !
- **Símbolos tipográficos**: guion (-), paréntesis ( ), comillas (")
- **Operadores matemáticos**: +, -, ×, *, ÷, /, =

### 📄 **5. Generación de Documentos PDF**

El sistema permite exportar las traducciones a archivos PDF de alta calidad con dos modalidades:

#### **Modo Normal**
- Texto original en español (fuente grande y centrada)
- Traducción en Braille Unicode debajo (con espaciado normalizado según estándares Braille)
- Formato A4 con márgenes profesionales
- Ideal para documentos de referencia o visualización en pantalla

#### **Modo Espejo (para impresión táctil)**
- Genera el contenido invertido horizontalmente (efecto espejo)
- Diseñado específicamente para impresión en relieve
- Cuando se imprime y se voltea el papel, el Braille queda orientado correctamente para lectura táctil
- **Espaciado optimizado**: 
  - Altura de celda: 10mm
  - Espaciado entre caracteres: 6mm
  - Espaciado entre líneas: 15mm
  - (Cumple con estándares de legibilidad táctil)

### 🎨 **6. Interfaz Gráfica Moderna (JavaFX)**

- **Diseño por pestañas**: Dos pestañas claramente diferenciadas:
  - **Pestaña 1**: Español → Braille (entrada de texto tradicional)
  - **Pestaña 2**: Braille → Español (celda Braille interactiva)
- **Celda Braille Visual**: 6 checkboxes grandes que simulan los puntos del sistema Braille en disposición 2×3.
- **Diseño intuitivo**: Interfaz sencilla y fácil de usar para cualquier usuario, con o sin conocimientos previos de Braille.
- **Notificaciones visuales**: Sistema de notificaciones minimalistas que confirman acciones exitosas o alertan sobre errores.
- **Instrucciones integradas**: Panel de ayuda visible en la pestaña de Braille con los controles de teclado disponibles.
- **Ventana redimensionable**: Tamaño inicial optimizado (800×750px) con posibilidad de ajuste según preferencias.
- **Acceso directo a funciones**: Botones claramente etiquetados para cada funcionalidad.

### 🔧 **7. Arquitectura Modular y Mantenible**

- **Patrón MVC**: Separación clara entre Modelo, Vista y Controlador.
- **Componentes especializados**:
  - `TraductorBraille`: Lógica de traducción bidireccional
  - `DiccionarioBraille`: Almacenamiento de correspondencias carácter-patrón con búsqueda bidireccional
  - `SimboloBraille`: Constantes y utilidades para símbolos Braille especiales
  - `GeneradorPDF`: Creación de documentos PDF en modo normal y espejo
  - `TraductorController`: Gestión de eventos de UI, celda Braille interactiva y coordinación de traducción
- **Interfaz `ITraductor`**: Permite extensibilidad para otros sistemas de traducción
- **Manejo de eventos**: Sistema robusto de captura de eventos de teclado y mouse para la celda Braille

### ⚙️ **8. Funcionalidades Avanzadas**

- **Detección automática de contexto**: El sistema reconoce automáticamente si un texto contiene números, mayúsculas o caracteres especiales y aplica las reglas correctas.
- **Normalización de espacios**: Manejo inteligente de espacios múltiples y saltos de línea.
- **Conversión patrón-Unicode bidireccional**: Sistema inteligente que convierte patrones de puntos (ej: "135") a caracteres Unicode Braille y viceversa.
- **Sincronización visual-lógica**: La celda Braille mantiene sincronizado el estado visual (checkboxes) con el estado interno (array de booleanos).
- **Generación de nombres automáticos**: Los PDFs generados incluyen marca de tiempo en el nombre del archivo para evitar sobrescrituras.
- **Entrada dual mouse/teclado**: Los usuarios pueden elegir el método de ingreso más cómodo para ellos.

---

## ⚙️ **9. Requisitos del sistema**

- JDK25 con javaFX (ZuluFX)
- Sistema operativo Windows

## 📦 **10. Descarga de Archivos Necesarios (Assets)**

Los archivos de la aplicación y el entorno de ejecución se encuentran en la sección **Releases (Versiones)** de este repositorio.

1.  Ve a la pestaña **Releases** o a la sección de **Tags**.
2.  Busca la versión **`v1.3.0`** (o la más reciente).
3.  Bajo la sección **Assets (Archivos Adjuntos)**, descarga los siguientes dos archivos:
    - `Traductor-braille-1.3.0.jar` (El programa principal).
    - `zulu25.30.17-ca-fx-jdk25.0.1-win_x64.msi` (El entorno de ejecución de Java/JavaFX necesario).

---

## 🔧 **11. Instalación del Entorno de Ejecución (ZuluFX)**

1. Abrir el archivo **msi** de Zulu.
2. Seguir el procesos de instalación.
3. Finalizar la instalación.

---

## 🚀 **12. Ejecución de la Aplicación**

1. Click derecho sobre el archivo `Traductor-braille-1.3.0.jar` y seleccionar "Abrir con" -> "Zulu Platform x64 Architecture"
2. ¡La aplicación **Traductor Braille** debería iniciarse!

---

